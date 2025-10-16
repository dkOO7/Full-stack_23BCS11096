import React, { useState } from "react";

const BookForm = ({ addBook }) => {
  const [book, setBook] = useState({ title: "", author: "" });

  const handleSubmit = (e) => {
    e.preventDefault();
    if (book.title && book.author) {
      addBook(book);
      setBook({ title: "", author: "" });
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginBottom: "20px" }}>
      <input
        type="text"
        placeholder="Book Title"
        value={book.title}
        onChange={(e) => setBook({ ...book, title: e.target.value })}
      />
      <input
        type="text"
        placeholder="Author"
        value={book.author}
        onChange={(e) => setBook({ ...book, author: e.target.value })}
      />
      <button type="submit">Add Book</button>
    </form>
  );
};

export default BookForm;
