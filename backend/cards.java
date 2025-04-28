name: Java 23 CI with Coverage Enforcement + PR Comment 

on:
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout code
      uses: actions/checkout@v4

    - name: Set up Java 23
      uses: actions/setup-java@v4
      with:
        distribution: 'temurin'
        java-version: '23'

    - name: Build with Maven (tests + JaCoCo)
      run: mvn clean verify

    - name: Parse Coverage
      id: coverage
      run: |
        COVERED=$(xmllint --xpath "string(//report/counter[@type='INSTRUCTION']/@covered)" target/site/jacoco/jacoco.xml)
        MISSED=$(xmllint --xpath "string(//report/counter[@type='INSTRUCTION']/@missed)" target/site/jacoco/jacoco.xml)
        TOTAL=$((COVERED + MISSED))
        PERCENT=$((100 * COVERED / TOTAL))
        echo "Coverage=$PERCENT" >> $GITHUB_ENV

    - name: Comment Coverage on PR
      uses: marocchino/sticky-pull-request-comment@v2
      with:
        header: coverage
        message: |
          - Coverage: **${{ env.Coverage }}%**
          - Required: **85%**

    - name: Fail if Coverage < 85%
      run: |
        if [ "${{ env.Coverage }}" -lt 85 ]; then
          echo "❌ Coverage is below 85%! Failing build."
          exit 1
        else
          echo "✅ Coverage is sufficient."
        fi

    - name: Upload Coverage Report
      uses: actions/upload-artifact@v4
      with:
        name: jacoco-report
        path: target/site/jacoco/
