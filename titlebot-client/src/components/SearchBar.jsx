import { useState } from 'react';

const SearchBar = ({ onGetPage }) => {
  const [url, setUrl] = useState('');

  const handleChange = (event) => {
      setUrl(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    onGetPage(url);
    setUrl('');
  };

  const isSearchTermValid = (searchTerm) => {
     return !searchTerm || searchTerm.startsWith("http") || searchTerm.startsWith("www")
  }

  return (
    <div className="search-form">
      <form onSubmit={handleSubmit}>
        <input value={url} onChange={handleChange} />
        <button disabled={isSearchTermValid(url)}>Search</button>
      </form>
    </div>
  );
}

export default SearchBar;