package db

case class Page(id: String, url: String, title: String, favIconUrl: String)

case class PageRequest(url: String, title: String, favIconUrl: String)
