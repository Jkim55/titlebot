
const PageRow = ({ page }) => {
  return (
    <li className="page-row">
      <img alt={page.url} src={page.favIconUrl}/>
      <div className="page-title">{ page.title }</div>
    </li>
  );
}

export default PageRow;
