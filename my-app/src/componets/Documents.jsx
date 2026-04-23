import { useState } from "react";
import "./upload.css";

export default function UploadPage() {
  const [files, setFiles] = useState([]);
  const [tempFile, setTempFile] = useState(null);
  const [tempPreview, setTempPreview] = useState(null);
  const [title, setTitle] = useState("");
  const [preview, setPreview] = useState(null);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    setTempFile(file);
    setTempPreview(URL.createObjectURL(file));
    setTitle("");
  };

  const saveFile = () => {
    if (!tempFile || !title) return;

    const newItem = {
      file: tempFile,
      name: title,
      preview: tempPreview,
    };

    setFiles((prev) => [...prev, newItem]);

    // reset modal state
    setTempFile(null);
    setTempPreview(null);
    setTitle("");
  };

  const removeFile = (index) => {
    const updated = [...files];
    URL.revokeObjectURL(updated[index].preview);
    updated.splice(index, 1);
    setFiles(updated);
  };

  return (
    <div className="upload-page">
      <h1>Documents</h1>

      {/* Upload */}
      <label className="upload-box">
        <input type="file" accept="image/*" onChange={handleFileChange} />
        <p>Click to upload image</p>
      </label>

      {/* Grid */}
      <div className="preview-grid">
        {files.map((item, index) => (
          <div key={index} className="preview-card">
            <img src={item.preview} onClick={() => setPreview(item.preview)} />
            <p>{item.name}</p>
            <button onClick={() => removeFile(index)}>Remove</button>
          </div>
        ))}
      </div>

      {/* 🔥 MODAL FOR TITLE */}
      {tempFile && (
        <div className="modal-overlay">
          <div className="modal-box">
            <h2>Name your document</h2>

            <img src={tempPreview} className="modal-preview" />

            <input
              type="text"
              placeholder="e.g. Hotel Receipt"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />

            <div className="modal-actions">
              <button onClick={() => setTempFile(null)}>Cancel</button>
              <button
                onClick={saveFile}
                disabled={!title.trim()}
                className="primary-btn"
              >
                Save
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Lightbox */}
      {preview && (
        <div className="image-modal" onClick={() => setPreview(null)}>
          <img src={preview} />
        </div>
      )}
    </div>
  );
}
