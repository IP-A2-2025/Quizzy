import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { api } from '../utils/api';
import './PDFViewer.css';

function PDFViewer() {
    // Extract the full path from the URL
    const location = useLocation();
    const fullPath = location.pathname.replace('/Material/path/', '');
    const stateTitle = location.state?.title;

    const [pageMode, setPageMode] = useState('single');
    const [pdfUrl, setPdfUrl] = useState('');
    const [objectUrl, setObjectUrl] = useState('');
    const [title, setTitle] = useState(stateTitle || 'PDF Viewer');
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [displayMode, setDisplayMode] = useState('object'); // 'iframe' or 'object'
    const [directUrl, setDirectUrl] = useState('');

    const fetchPdf = async () => {
        try {
            console.log('Full PDF Path:', fullPath);
            // Make sure path is properly decoded
            const path = decodeURIComponent(fullPath);

            // The PDF path should start with "cursuri" and end with ".pdf"
            const pathParts = path.split('/');

            if (pathParts.length >= 3 && pathParts[0] === 'cursuri') {
                const courseTitle = pathParts[1];
                const fileName = pathParts[pathParts.length - 1]; // Get the last part (PDF filename)

                // Set title from filename if not provided in state
                if (!stateTitle) {
                    setTitle(fileName.replace('.pdf', '') || 'Document');
                }

                // Create direct URL for fallback - use the full path as received
                const baseUrl = window.location.origin;
                const apiEndpoint = `/Material/path/${path}`;
                const directPdfUrl = `${baseUrl}${apiEndpoint}`;
                setDirectUrl(directPdfUrl);

                console.log('Fetching PDF from API endpoint:', apiEndpoint);

                // Use the api.getBinaryFile method to fetch the PDF
                const response = await api.getBinaryFile(apiEndpoint, {
                    'Accept': 'application/pdf'
                });

                console.log('PDF response received:', response.status);

                if (response.data && response.data.size > 0) {
                    // Create a blob URL
                    const blob = new Blob([response.data], { type: 'application/pdf' });
                    const blobUrl = URL.createObjectURL(blob);

                    console.log('Blob URL created, size:', blob.size);
                    setObjectUrl(blobUrl);
                    setPdfUrl(blobUrl);
                } else {
                    console.warn('Empty PDF response received');
                    // Fall back to direct URL
                    setPdfUrl(directPdfUrl);
                }
            } else {
                setError('Invalid PDF path format. Expected: cursuri/{courseTitle}/.../{fileName}.pdf');
                console.error('Invalid PDF path format:', path);
            }
        } catch (err) {
            console.error('Error fetching PDF:', err);
            
            // Try direct URL as fallback
            if (directUrl) {
                console.log('Using direct URL as fallback');
                setPdfUrl(directUrl);
            } else {
                setError(`Failed to load PDF: ${err.message || 'Unknown error'}`);
            }
        } finally {
            setLoading(false);
        }
    };
    
    useEffect(() => {
        if (fullPath) {
            fetchPdf();
        }
        
        return () => {
            // Clean up blob URL when component unmounts
            if (objectUrl) {
                URL.revokeObjectURL(objectUrl);
            }
        };
    }, [fullPath]);

    // Function to toggle between iframe and object display modes
    const toggleDisplayMode = () => {
        setDisplayMode(prevMode => prevMode === 'iframe' ? 'object' : 'iframe');
    };

    return (
        <div className="App">
            <header className="App-header">
                <div className="App-container">
                    <div className="Pdf-header">
                        <div className="Pdf-section dark">
                            <h1 className="Pdf-title">{title}</h1>
                            {error && <p className="Pdf-error">{error}</p>}
                            {directUrl && (
                                <p className="Pdf-direct-link">
                                    <a 
                                        href={directUrl}
                                        target="_blank"
                                        rel="noopener noreferrer"
                                        style={{color: 'white', textDecoration: 'underline'}}
                                    >
                                        Open PDF in New Tab
                                    </a>
                                </p>
                            )}
                        </div>

                        <div className="Pdf-section light">
                            <div className="page-toggle-buttons">
                                <button
                                    className={`toggle-btn ${pageMode === 'single' ? 'active' : ''}`}
                                    onClick={() => setPageMode('single')}
                                    title="Single Page"
                                >
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                                        <path d="M4 4H20V20H4V4ZM6 6V18H18V6H6Z" />
                                    </svg>
                                </button>
                                <button
                                    className={`toggle-btn ${pageMode === 'continuous' ? 'active' : ''}`}
                                    onClick={() => setPageMode('continuous')}
                                    title="Continuous Pages"
                                >
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                                        <path d="M3 3H9V9H3V3ZM15 3H21V9H15V3ZM3 15H9V21H3V15ZM15 15H21V21H15V15Z" />
                                    </svg>
                                </button>
                                <button 
                                    className="toggle-btn"
                                    onClick={toggleDisplayMode} 
                                    title="Toggle Display Mode"
                                >
                                    {displayMode === 'iframe' ? 'Use Object Tag' : 'Use Iframe'}
                                </button>
                            </div>
                        </div>
                    </div>

                    <div
                        id="iframe-wrapper"
                        style={{
                            width: '100%',
                            height: '800px',
                            overflow: pageMode === 'single' ? 'hidden' : 'auto',
                        }}
                    >
                        {loading ? (
                            <div className="pdf-loading">Loading PDF...</div>
                        ) : pdfUrl ? (
                            displayMode === 'iframe' ? (
                                <iframe
                                    id="pdf-iframe"
                                    title="PDF Preview"
                                    src={pdfUrl}
                                    width="100%"
                                    height="100%"
                                    style={{ border: 'none' }}
                                    allowFullScreen={true}
                                />
                            ) : (
                                <object
                                    data={pdfUrl}
                                    type="application/pdf"
                                    width="100%"
                                    height="100%"
                                >
                                    <p>Your browser does not support PDFs. 
                                        <a href={pdfUrl} download={`${title}.pdf`}>Download the PDF</a> instead.
                                    </p>
                                </object>
                            )
                        ) : (
                            <div className="pdf-loading">
                                {error ? 'Failed to load PDF.' : 'Preparing PDF...'}
                            </div>
                        )}
                    </div>
                </div>
            </header>
        </div>
    );
}

export default PDFViewer;