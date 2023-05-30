import { useState, useEffect } from 'react';
import axios from 'axios';
import SearchBar from './components/SearchBar';
import PageList from './components/PageList';

const App = () => {
  const [pages, setPages] = useState([]);

  const fetchPages = async () => {
    const response = await axios.get('http://localhost:9000/pages');
    setPages(response.data);
  };

  useEffect(() => {
    fetchPages();
  }, []);

  const getPage = async (pageUrl) => {
    try {
      const response = await axios.get(`http://localhost:9000/pages?pageUrl=${pageUrl}`);
      return response
    } catch (error) {
      throw new Error(error)
    }
  };

  const handleGetPage = async (pageUrl) => {
  getPage(pageUrl)
    .then(({ status, data }) => {
      setPages([...pages, data])
      })
    .catch((err: Error) => console.log(err))
  }

  return (
    <div className="app">
      <h1 className="app-header">TitleBot</h1>
      <SearchBar onGetPage={handleGetPage} />
      <PageList pages={pages}/>
    </div>
  );
}

export default App;
