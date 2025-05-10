# Cum să configurezi backend-ul pentru **Quizzy**

Acest ghid te va ajuta să configurezi backend-ul pentru proiectul **Quizzy** pas cu pas.

## Partea 1: Clonarea Repository-ului

1. Deschide GitHub-ul și accesează repository-ul **'Quizzy'**.
2. Apasă pe butonul albastru **`<> CODE`** și copiază URL-ul de clonare al repository-ului.
3. Deschide un terminal și navighează în locația unde dorești să clonezi repository-ul folosind comanda `cd <calea_dorita>`.
4. Rulează comanda următoare pentru a clona repository-ul:
   ```bash
   git clone https://github.com/IP-A2-2025/Quizzy.git
5. Deschide **Intelij**:
   - Mergi la **File** -> **Open Project**.
   - Căută folderul **Quizzy** unde l-ai clonat.
   - Accesează folderul **'backend'** și apasă pe **Open**.
  
## Partea 2: Configuratea Inteliji
1. În IntelliJ, mergi la **File** -> **Project Structure**.
2. În secțiunea **Platform Settings** -> **SDKs**, apasă pe **+** și alege **Download JDK**.
3. Căută **Amazon Corretto** (pentru **Mac**) sau **JDK 23** (pentru **Windows**) la **Vendor** și alege **Version 23**. Apasă pe **Download**.
4. După finalizarea instalării, în fereastra **Project Structure**, accesează **Project** și selectează **Corretto-23** (Mac)/ **JDK 23** (Windows) la câmpul **SDK**.
5. Apasă **OK** pentru a salva modificările.

## Partea 3: Configurarea Aplicației Spring Boot
1. În fereastra principală, mergi sus și caută **Backend Application**.
2. Apasă pe **Edit Configurations**.
3. Apasă pe **+** în partea stângă sus și alege **Spring Boot**.
4. La câmpul **Name**, scrie **'Backend'**.
5. La câmpul **Main Class**, scrie: `com.backend.BackendApplication`.
6. Apoi o sa trebuiasca sa dai copy la niste informatii trimise in privat.
7. La secțiunea **Build and Run**, apasă pe **Modify options** -> **Environment variables**.
8. Apasă pe iconița cea mai din dreapta din campul nou aparut (seamana cu o lista) și lipește variabilele de mediu (la care ai dat copy) în fereastra care apare.
9. Apasă **OK** și apoi **Apply**.

### Partea 4: Rulează Aplicația
1. În fereastra principală, în partea dreaptă, vei vedea mai multe iconițe, printre care și iconița **M** (de la Maven).
2. Apasă pe **M** -> **backend** -> **lifecycle** și apasă pe **clean**.
3. Dacă totul a fost configurat corect, în terminal vei vedea mesajul **'Process finished with exit code 0'**, ceea ce înseamnă că procesul a avut succes.

---

**Felicitări!** Acum ai configurat corect backend-ul pentru **Quizzy**. Dacă întâmpini dificultăți, nu ezita să ceri ajutor!


# Setup S3 Bucket

## Instalare CLI (AWS Command Line)
🛠️ Acești pași sunt necesari doar pentru testarea locală a codului. Când aplicația va fi rulată în cloud, AWS CLI și configurarea manuală NU mai sunt necesare.

1. Descarcă AWS CLI de la [acest link](https://awscli.amazonaws.com/AWSCLIV2.msi).
2. După instalare, deschide un terminal și rulează comanda:
   ```bash
   aws configure
   ```
   La regiune scrieți eu-central-1
   ```
   AWS Access Key ID [********************]: ...
   AWS Secret Access Key [********************]: ...
   Default region name [eu-central-1]: eu-central-1
   Default output format [json]: json
   ```
   
3. Toate informațiile necesare de contectare le vei primii in privat.

4. Incearcă să rulezi comanda 
 ```bash
   aws s3 ls s3://quizzy-s3-bucket
```

## Setup cu Intelij
1. Asigură-te că sunt aceste două dependențe în pom.xml
 ```java
     <dependency>
           <groupId>software.amazon.awssdk</groupId>
           <artifactId>s3</artifactId>
           <version>2.25.43</version>
       </dependency>
       <dependency>
           <groupId>org.slf4j</groupId>
           <artifactId>slf4j-simple</artifactId>
           <version>1.7.30</version>
       </dependency>
```
2. Cod de test:
```java
package org.example;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.S3Object;

public class s3test {
    public static void main(String[] args) {
        String bucketName = "quizzy-s3-bucket";

        S3Client s3 = S3Client.builder()
                .region(Region.of("eu-central-1"))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        s3.listObjectsV2(request).contents().forEach((S3Object obj) -> {
            System.out.println(obj.key() + " (" + obj.size() + " bytes)");
        });

        s3.close();
    }
}
```
Bravo!! Ai reusit!
## Cum adaugi un curs in bucket?
Poti adăuga un fișier direct din terminal:
```bash
   aws s3 cp C:\cale\spre\fisier.pdf s3://quizzy-s3-bucket/folder/fisier.pdf
```

Sau poți încărca un fișier din java, astfel:

Exemplu de cod pt a încărca un curs într-un S3:
```java
package org.example;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Paths;

public class UploadToS3 {
    public static void main(String[] args) {
        String bucketName = "quizzy-s3-bucket";
        String key = "curs.pdf";
        String filePath = "C:\\cale\\spre\\fisier\\curs.pdf";


        S3Client s3 = S3Client.builder()
                .region(Region.of("eu-central-1"))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3.putObject(request, RequestBody.fromFile(Paths.get(filePath)));

        s3.close();
    }
}
```

Exemplu de cod pt a descărca un curs dintr-un S3:
```java
package org.example;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.nio.file.Path;

public class DownloadS3 {
    public static void main(String[] args) {
        String bucketName = "quizzy-s3-bucket";
        String key = "cursuri/curs.pdf";
        String downloadPath = "C:\\cale\\spre\\download\\curs.pdf"; // Local path to save the file

        S3Client s3 = S3Client.builder()
                .region(Region.of("eu-central-1"))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        try {
            // Create a request to get the file from S3
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            // Download the file to the specified path
            GetObjectResponse response = s3.getObject(getObjectRequest, Path.of(downloadPath));
            System.out.println("File downloaded to: " + downloadPath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s3.close();
        }
    }
}
```
