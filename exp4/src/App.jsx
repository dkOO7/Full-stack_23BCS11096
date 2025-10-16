import React, { useState } from "react";
import BookList from "./components/BookList";
import BookForm from "./components/BookForm";
import SearchBar from "./components/SearchBar";

const App = () => {
  const [books, setBooks] = useState([
    { id: 1, title: "The Alchemist", author: "Paulo Coelho" },
    { id: 2, title: "Atomic Habits", author: "James Clear" }
  ]);
  
  const [searchQuery, setSearchQuery] = useState("");

  const addBook = (newBook) => {
    const id = books.length > 0 ? books[books.length - 1].id + 1 : 1;
    setBooks([...books, { id, ...newBook }]);
  };

  const updateBook = (updatedBook) => {
    setBooks(
      books.map((book) =>
        book.id === updatedBook.id ? updatedBook : book
      )
    );
  };

  const deleteBook = (id) => {
    setBooks(books.filter((book) => book.id !== id));
  };

  const filteredBooks = books.filter((book) =>
    book.title.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div style={{ padding: "20px" }}>
      <h1>📚 Library Management</h1>
      <SearchBar setSearchQuery={setSearchQuery} />
      <BookForm addBook={addBook} />
      <BookList
        books={filteredBooks}
        deleteBook={deleteBook}
        updateBook={updateBook}
      />
    </div>
  );
};

export default App;
