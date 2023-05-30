import PageRow from './PageRow';

const PageList = ({ pages }) => {
  const renderedPages = pages.map((page) => {
    return <PageRow key={page.id} page={page}/>
  });

  return (
    <div className="list-container">
      <ul className="page-list">
        {renderedPages}
      </ul>
    </div>
  )
}

export default PageList;
