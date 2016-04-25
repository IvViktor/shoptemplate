<%@page language='java' contentType='text/html'%>
<%@page import='sitecreators.utils.product.Product' %>
<%@page import='sitecreators.utils.product.ProductDAO' %>
<%@page import='sitecreators.utils.user.User' %>
<%@page import='sitecreators.utils.user.UserDAO' %>
<%@page import='sitecreators.utils.order.Order' %>
<%@page import='sitecreators.utils.order.OrderDAO' %>
<%@page import='sitecreators.utils.finance.Currency' %>
<%@page import='sitecreators.utils.finance.CurrencyDAO' %>
<%@page import='sitecreators.utils.category.Category' %>
<%@page import='sitecreators.utils.category.CategoryDAO' %>
<%@page import='sitecreators.utils.ApplicationContextUtil' %>
<%@page import='java.util.List' %>
<%
CategoryDAO categorydao = (CategoryDAO) ApplicationContextUtil.getApplicationContext().getBean("CategoryDAO");
ProductDAO productdao = (ProductDAO) ApplicationContextUtil.getApplicationContext().getBean("ProductDAO");
UserDAO userdao = (UserDAO) ApplicationContextUtil.getApplicationContext().getBean("UserDAO");
CurrencyDAO currencydao = (CurrencyDAO) ApplicationContextUtil.getApplicationContext().getBean("CurrencyDAO");
OrderDAO orderdao = (OrderDAO) ApplicationContextUtil.getApplicationContext().getBean("OrderDAO");
List<Category> categories = categorydao.getAllCategories();
List<Currency> currencies = currencydao.getAllCurrencies();
List<Order> orders = orderdao.getOrders();
List<Product> products = productdao.getProducts();
List<User> users = userdao.getUsers();
%>
<html>
<body>
<h1>Hello World!</h1>
<h2>Number of categories:<%= categories.size() %></h2><br/>
<h2>Number of currencies:<%= currencies.size() %></h2><br/>
<h2>Number of orders:<%= orders.size() %></h2><br/>
<h2>Number of products:<%= products.size() %></h2><br/>
<h2>Number of users:<%= users.size() %></h2><br/>
</body>
</html>
