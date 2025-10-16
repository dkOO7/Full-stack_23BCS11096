import React, { useState } from "react";

const BookList = ({ books, deleteBook, updateBook }) => {
  const [editing, setEditing] = useState(null);
  const [updatedData, setUpdatedData] = useState({});

  const handleEdit = (book) => {
    setEditing(book.id);
    setUpdatedData(book);
  };

  const handleSave = () => {
    updateBook(updatedData);
    setEditing(null);
  };

  return (
    <div>
      <h2>Book List</h2>
      {books.length === 0 ? (
        <p>No books found.</p>
      ) : (
        books.map((book) => (
          <div
            key={book.id}
            style={{
              border: "1px solid gray",
              padding: "10px",
              marginBottom: "10px",
            }}
          >
            {editing === book.id ? (
              <>
                <input
                  type="text"
                  value={updatedData.title}
                  onChange={(e) =>
                    setUpdatedData({ ...updatedData, title: e.target.value })
                  }
                />
                <input
                  type="text"
                  value={updatedData.author}
                  onChange={(e) =>
                    setUpdatedData({ ...updatedData, author: e.target.value })
                  }
                />
                <button onClick={handleSave}>Save</button>
                <button onClick={() => setEditing(null)}>Cancel</button>
              </>
            ) : (
              <>
                <h3>{book.title}</h3>
                <p>Author: {book.author}</p>
                <button onClick={() => handleEdit(book)}>Edit</button>
                <button onClick={() => deleteBook(book.id)}>Delete</button>
              </>
            )}
          </div>
        ))
      )}
    </div>
  );
};

export default BookList;
