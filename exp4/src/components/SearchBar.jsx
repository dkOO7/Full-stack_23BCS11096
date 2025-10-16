import React from "react";

const SearchBar = ({ setSearchQuery }) => {
  return (
    <input
      type="text"
      placeholder="Search by title..."
      onChange={(e) => setSearchQuery(e.target.value)}
      style={{ marginBottom: "20px", width: "300px", height: "30px" }}
    />
  );
};

export default SearchBar;
