--==============================================================
-- DBMS name:      ANSI Level 2
-- Created on:     2010/12/13 22:02:47
--==============================================================


--==============================================================
-- Table: Categories
--==============================================================
create table Categories (
ID                   INT                  not null,
Category_Name        VARCHAR(15)          not null,
Parent_ID            INT,
Description          VARCHAR(254),
primary key (ID),
foreign key (Parent_ID)
      references Categories (ID)
);

--==============================================================
-- Index: I_Categories_CategoryName
--==============================================================
create  index I_Categories_CategoryName on Categories (
Category_Name ASC
);

--==============================================================
-- Table: Customers
--==============================================================
create table Customers (
ID                   CHAR(5)              not null,
Company_Name         VARCHAR(40)          not null,
Contact_Name         VARCHAR(30),
Contact_Title        VARCHAR(30),
Address              VARCHAR(60),
City                 VARCHAR(15),
Region               VARCHAR(15),
Postal_Code          VARCHAR(10),
Country              VARCHAR(15),
Phone                VARCHAR(24),
Fax                  VARCHAR(24),
primary key (ID)
);

--==============================================================
-- Index: I_Customers_City
--==============================================================
create  index I_Customers_City on Customers (
City ASC
);

--==============================================================
-- Index: I_Customers_CompanyName
--==============================================================
create  index I_Customers_CompanyName on Customers (
Company_Name ASC
);

--==============================================================
-- Index: I_Customers_PostalCode
--==============================================================
create  index I_Customers_PostalCode on Customers (
Postal_Code ASC
);

--==============================================================
-- Index: I_Customers_Region
--==============================================================
create  index I_Customers_Region on Customers (
Region ASC
);

--==============================================================
-- Table: Employees
--==============================================================
create table Employees (
ID                   INT                  not null,
Last_Name            VARCHAR(20)          not null,
First_Name           VARCHAR(10)          not null,
Title                VARCHAR(30),
Title_Of_Courtesy    VARCHAR(25),
Sex                  SMALLINT,
Birth_Date           DATE,
Hire_Date            DATE,
Address              VARCHAR(60),
City                 VARCHAR(15),
Region               VARCHAR(15),
Postal_Code          VARCHAR(10),
Country              VARCHAR(15),
Phone                VARCHAR(24),
Extension            VARCHAR(4),
Reports_To           INT,
Notes                VARCHAR(1024),
Photo_Path           VARCHAR(255),
primary key (ID),
foreign key (Reports_To)
      references Employees (ID)
);

--==============================================================
-- Index: I_Employees_LastName
--==============================================================
create  index I_Employees_LastName on Employees (
Last_Name ASC
);

--==============================================================
-- Index: I_Employees_PostalCode
--==============================================================
create  index I_Employees_PostalCode on Employees (
Postal_Code ASC
);

--==============================================================
-- Table: Shippers
--==============================================================
create table Shippers (
ID                   INT                  not null,
Company_Name         VARCHAR(40)          not null,
Phone                VARCHAR(24),
primary key (ID)
);

--==============================================================
-- Table: Orders
--==============================================================
create table Orders (
ID                   INT                  not null,
Customer_ID          CHAR(5),
Employee_ID          INT,
Order_Date           DATE,
Required_Date        DATE,
Shipped_Date         DATE,
Ship_Via             INT,
Freight              NUMERIC(8,2)          default 0,
Ship_Name            VARCHAR(40),
Ship_Address         VARCHAR(60),
Ship_City            VARCHAR(15),
Ship_Region          VARCHAR(15),
Ship_Postal_Code     VARCHAR(10),
Ship_Country         VARCHAR(15),
primary key (ID),
foreign key (Customer_ID)
      references Customers (ID),
foreign key (Employee_ID)
      references Employees (ID),
foreign key (Ship_Via)
      references Shippers (ID)
);

--==============================================================
-- Table: Suppliers
--==============================================================
create table Suppliers (
ID                   INT                  not null,
Company_Name         VARCHAR(40)          not null,
Contact_Name         VARCHAR(30),
Contact_Title        VARCHAR(30),
Address              VARCHAR(60),
City                 VARCHAR(15),
Region               VARCHAR(15),
Postal_Code          VARCHAR(10),
Country              VARCHAR(15),
Phone                VARCHAR(24),
Fax                  VARCHAR(24),
Home_Page            VARCHAR(254),
primary key (ID)
);

--==============================================================
-- Table: Products
--==============================================================
create table Products (
ID                   INT                  not null,
Product_Name         VARCHAR(40)          not null,
Supplier_ID          INT,
Category_ID          INT,
Quantity_Per_Unit    VARCHAR(20),
Unit_Price           NUMERIC(8,2)          default 0,
Units_In_Stock       INT                   default 0,
Units_On_Order       INT                   default 0,
Reorder_Level        INT                   default 0,
Discontinued         SMALLINT             not null default 0,
primary key (ID),
foreign key (Category_ID)
      references Categories (ID),
foreign key (Supplier_ID)
      references Suppliers (ID)
);

--==============================================================
-- Table: Order_Details
--==============================================================
create table Order_Details (
ID                   INT                  not null,
Order_ID             INT                  not null,
Product_ID           INT                  not null,
Unit_Price           NUMERIC(8,2)         not null default 0,
Quantity             INT                  not null default 1,
Discount             DECIMAL              not null default 0,
primary key (ID),
foreign key (Order_ID)
      references Orders (ID),
foreign key (Product_ID)
      references Products (ID),
check (Discount >= 0 and (Discount <= 1))
);

--==============================================================
-- Index: I_OrderDetails_OrderID
--==============================================================
create  index I_OrderDetails_OrderID on Order_Details (
Order_ID ASC
);

--==============================================================
-- Index: I_OrderDetails_ProductID
--==============================================================
create  index I_OrderDetails_ProductID on Order_Details (
Product_ID ASC
);

--==============================================================
-- Index: I_Orders_CustomerID
--==============================================================
create  index I_Orders_CustomerID on Orders (
Customer_ID ASC
);

--==============================================================
-- Index: I_Orders_CustomersOrders
--==============================================================
create  index I_Orders_CustomersOrders on Orders (
Customer_ID ASC
);

--==============================================================
-- Index: I_Orders_EmployeeID
--==============================================================
create  index I_Orders_EmployeeID on Orders (
Employee_ID ASC
);

--==============================================================
-- Index: I_Orders_EmployeesOrders
--==============================================================
create  index I_Orders_EmployeesOrders on Orders (
Employee_ID ASC
);

--==============================================================
-- Index: I_Orders_OrderDate
--==============================================================
create  index I_Orders_OrderDate on Orders (
Order_Date ASC
);

--==============================================================
-- Index: I_Orders_ShippedDate
--==============================================================
create  index I_Orders_ShippedDate on Orders (
Shipped_Date ASC
);

--==============================================================
-- Index: I_Orders_ShippersOrders
--==============================================================
create  index I_Orders_ShippersOrders on Orders (
Ship_Via ASC
);

--==============================================================
-- Index: I_Orders_ShipPostalCode
--==============================================================
create  index I_Orders_ShipPostalCode on Orders (
Ship_Postal_Code ASC
);

--==============================================================
-- Index: I_Products_CategoriesProducts
--==============================================================
create  index I_Products_CategoriesProducts on Products (
Category_ID ASC
);

--==============================================================
-- Index: I_Products_CategoryID
--==============================================================
create  index I_Products_CategoryID on Products (
Category_ID ASC
);

--==============================================================
-- Index: I_Products_ProductName
--==============================================================
create  index I_Products_ProductName on Products (
Product_Name ASC
);

--==============================================================
-- Index: I_Products_SupplierID
--==============================================================
create  index I_Products_SupplierID on Products (
Supplier_ID ASC
);

--==============================================================
-- Index: I_Products_SuppliersProducts
--==============================================================
create  index I_Products_SuppliersProducts on Products (
Supplier_ID ASC
);

--==============================================================
-- Index: I_Suppliers_CompanyName
--==============================================================
create  index I_Suppliers_CompanyName on Suppliers (
Company_Name ASC
);

--==============================================================
-- Index: I_Suppliers_PostalCode
--==============================================================
create  index I_Suppliers_PostalCode on Suppliers (
Postal_Code ASC
);

CREATE SEQUENCE SEQ_ID;

INSERT INTO CATEGORIES
(ID, CATEGORY_NAME, PARENT_ID, DESCRIPTION)
VALUES
(1, 'Beverages', NULL, 'Soft drinks, coffees, teas, beers, and ales');

INSERT INTO CATEGORIES
(ID, CATEGORY_NAME, PARENT_ID, DESCRIPTION)
VALUES
(2, 'Condiments', NULL, 'Sweet and savory sauces, relishes, spreads, and seasonings');

INSERT INTO CATEGORIES
(ID, CATEGORY_NAME, PARENT_ID, DESCRIPTION)
VALUES
(3, 'Confections', NULL, 'Desserts, candies, and sweet breads');

INSERT INTO CATEGORIES
(ID, CATEGORY_NAME, PARENT_ID, DESCRIPTION)
VALUES
(4, 'Dairy Products', NULL, 'Cheeses');

INSERT INTO CATEGORIES
(ID, CATEGORY_NAME, PARENT_ID, DESCRIPTION)
VALUES
(5, 'Grains/Cereals', NULL, 'Breads, crackers, pasta, and cereal');

INSERT INTO CATEGORIES
(ID, CATEGORY_NAME, PARENT_ID, DESCRIPTION)
VALUES
(6, 'Meat/Poultry', NULL, 'Prepared meats');

INSERT INTO CATEGORIES
(ID, CATEGORY_NAME, PARENT_ID, DESCRIPTION)
VALUES
(7, 'Produce', NULL, 'Dried fruit and bean curd');

INSERT INTO CATEGORIES
(ID, CATEGORY_NAME, PARENT_ID, DESCRIPTION)
VALUES
(8, 'Seafood', NULL, 'Seaweed and fish');

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'ALFKI'
  , 'Alfreds Futterkiste'
  , 'Maria Anders'
  , 'Sales Representative'
  , 'Obere Str. 57'
  , 'Berlin'
  , NULL
  , '12209'
  , 'Germany'
  , '030-0074321'
  , '030-0076545'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'ANATR'
  , 'Ana Trujillo Emparedados y helados'
  , 'Ana Trujillo'
  , 'Owner'
  , 'Avda. de la Constitución 2222'
  , 'México D.F.'
  , NULL
  , '05021'
  , 'Mexico'
  , '(5) 555-4729'
  , '(5) 555-3745'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'ANTON'
  , 'Antonio Moreno Taquería'
  , 'Antonio Moreno'
  , 'Owner'
  , 'Mataderos  2312'
  , 'México D.F.'
  , NULL
  , '05023'
  , 'Mexico'
  , '(5) 555-3932'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'AROUT'
  , 'Around the Horn'
  , 'Thomas Hardy'
  , 'Sales Representative'
  , '120 Hanover Sq.'
  , 'London'
  , NULL
  , 'WA1 1DP'
  , 'UK'
  , '(171) 555-7788'
  , '(171) 555-6750'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'BERGS'
  , 'Berglunds snabbk?p'
  , 'Christina Berglund'
  , 'Order Administrator'
  , 'Berguvsv?gen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
  , '0921-12 34 65'
  , '0921-12 34 67'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'BLAUS'
  , 'Blauer See Delikatessen'
  , 'Hanna Moos'
  , 'Sales Representative'
  , 'Forsterstr. 57'
  , 'Mannheim'
  , NULL
  , '68306'
  , 'Germany'
  , '0621-08460'
  , '0621-08924'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'BLONP'
  , 'Blondel père et fils'
  , 'Frédérique Citeaux'
  , 'Marketing Manager'
  , '24, place Kléber'
  , 'Strasbourg'
  , NULL
  , '67000'
  , 'France'
  , '88.60.15.31'
  , '88.60.15.32'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'BOLID'
  , 'Bólido Comidas preparadas'
  , 'Martín Sommer'
  , 'Owner'
  , 'C/ Araquil, 67'
  , 'Madrid'
  , NULL
  , '28023'
  , 'Spain'
  , '(91) 555 22 82'
  , '(91) 555 91 99'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'BONAP'
  , 'Bon app'''
  , 'Laurence Lebihan'
  , 'Owner'
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
  , '91.24.45.40'
  , '91.24.45.41'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'BOTTM'
  , 'Bottom-Dollar Markets'
  , 'Elizabeth Lincoln'
  , 'Accounting Manager'
  , '23 Tsawassen Blvd.'
  , 'Tsawassen'
  , 'BC'
  , 'T2F 8M4'
  , 'Canada'
  , '(604) 555-4729'
  , '(604) 555-3745'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'BSBEV'
  , 'B''s Beverages'
  , 'Victoria Ashworth'
  , 'Sales Representative'
  , 'Fauntleroy Circus'
  , 'London'
  , NULL
  , 'EC2 5NT'
  , 'UK'
  , '(171) 555-1212'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'CACTU'
  , 'Cactus Comidas para llevar'
  , 'Patricio Simpson'
  , 'Sales Agent'
  , 'Cerrito 333'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
  , '(1) 135-5555'
  , '(1) 135-4892'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'CENTC'
  , 'Centro comercial Moctezuma'
  , 'Francisco Chang'
  , 'Marketing Manager'
  , 'Sierras de Granada 9993'
  , 'México D.F.'
  , NULL
  , '05022'
  , 'Mexico'
  , '(5) 555-3392'
  , '(5) 555-7293'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'CHOPS'
  , 'Chop-suey Chinese'
  , 'Yang Wang'
  , 'Owner'
  , 'Hauptstr. 29'
  , 'Bern'
  , NULL
  , '3012'
  , 'Switzerland'
  , '0452-076545'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'COMMI'
  , 'Comércio Mineiro'
  , 'Pedro Afonso'
  , 'Sales Associate'
  , 'Av. dos Lusíadas, 23'
  , 'S?o Paulo'
  , 'SP'
  , '05432-043'
  , 'Brazil'
  , '(11) 555-7647'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'CONSH'
  , 'Consolidated Holdings'
  , 'Elizabeth Brown'
  , 'Sales Representative'
  , 'Berkeley Gardens
12  Brewery '
  , 'London'
  , NULL
  , 'WX1 6LT'
  , 'UK'
  , '(171) 555-2282'
  , '(171) 555-9199'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'DRACD'
  , 'Drachenblut Delikatessen'
  , 'Sven Ottlieb'
  , 'Order Administrator'
  , 'Walserweg 21'
  , 'Aachen'
  , NULL
  , '52066'
  , 'Germany'
  , '0241-039123'
  , '0241-059428'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'DUMON'
  , 'Du monde entier'
  , 'Janine Labrune'
  , 'Owner'
  , '67, rue des Cinquante Otages'
  , 'Nantes'
  , NULL
  , '44000'
  , 'France'
  , '40.67.88.88'
  , '40.67.89.89'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'EASTC'
  , 'Eastern Connection'
  , 'Ann Devon'
  , 'Sales Agent'
  , '35 King George'
  , 'London'
  , NULL
  , 'WX3 6FW'
  , 'UK'
  , '(171) 555-0297'
  , '(171) 555-3373'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'ERNSH'
  , 'Ernst Handel'
  , 'Roland Mendel'
  , 'Sales Manager'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
  , '7675-3425'
  , '7675-3426'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'FAMIA'
  , 'Familia Arquibaldo'
  , 'Aria Cruz'
  , 'Marketing Assistant'
  , 'Rua Orós, 92'
  , 'S?o Paulo'
  , 'SP'
  , '05442-030'
  , 'Brazil'
  , '(11) 555-9857'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'FISSA'
  , 'FISSA Fabrica Inter. Salchichas S.A.'
  , 'Diego Roel'
  , 'Accounting Manager'
  , 'C/ Moralzarzal, 86'
  , 'Madrid'
  , NULL
  , '28034'
  , 'Spain'
  , '(91) 555 94 44'
  , '(91) 555 55 93'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'FOLIG'
  , 'Folies gourmandes'
  , 'Martine Ranc?'
  , 'Assistant Sales Agent'
  , '184, chaussée de Tournai'
  , 'Lille'
  , NULL
  , '59000'
  , 'France'
  , '20.16.10.16'
  , '20.16.10.17'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'FOLKO'
  , 'Folk och f?HB'
  , 'Maria Larsson'
  , 'Owner'
  , '?kergatan 24'
  , 'Br?cke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
  , '0695-34 67 21'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'FRANK'
  , 'Frankenversand'
  , 'Peter Franken'
  , 'Marketing Manager'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
  , '089-0877310'
  , '089-0877451'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'FRANR'
  , 'France restauration'
  , 'Carine Schmitt'
  , 'Marketing Manager'
  , '54, rue Royale'
  , 'Nantes'
  , NULL
  , '44000'
  , 'France'
  , '40.32.21.21'
  , '40.32.21.20'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'FRANS'
  , 'Franchi S.p.A.'
  , 'Paolo Accorti'
  , 'Sales Representative'
  , 'Via Monte Bianco 34'
  , 'Torino'
  , NULL
  , '10100'
  , 'Italy'
  , '011-4988260'
  , '011-4988261'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'FURIB'
  , 'Furia Bacalhau e Frutos do Mar'
  , 'Lino Rodriguez '
  , 'Sales Manager'
  , 'Jardim das rosas n. 32'
  , 'Lisboa'
  , NULL
  , '1675'
  , 'Portugal'
  , '(1) 354-2534'
  , '(1) 354-2535'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'GALED'
  , 'Galería del gastrónomo'
  , 'Eduardo Saavedra'
  , 'Marketing Manager'
  , 'Rambla de Catalu?a, 23'
  , 'Barcelona'
  , NULL
  , '08022'
  , 'Spain'
  , '(93) 203 4560'
  , '(93) 203 4561'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'GODOS'
  , 'Godos Cocina Típica'
  , 'Jos?Pedro Freyre'
  , 'Sales Manager'
  , 'C/ Romero, 33'
  , 'Sevilla'
  , NULL
  , '41101'
  , 'Spain'
  , '(95) 555 82 82'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'GOURL'
  , 'Gourmet Lanchonetes'
  , 'Andr?Fonseca'
  , 'Sales Associate'
  , 'Av. Brasil, 442'
  , 'Campinas'
  , 'SP'
  , '04876-786'
  , 'Brazil'
  , '(11) 555-9482'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'GREAL'
  , 'Great Lakes Food Market'
  , 'Howard Snyder'
  , 'Marketing Manager'
  , '2732 Baker Blvd.'
  , 'Eugene'
  , 'OR'
  , '97403'
  , 'USA'
  , '(503) 555-7555'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'GROSR'
  , 'GROSELLA-Restaurante'
  , 'Manuel Pereira'
  , 'Owner'
  , '5?Ave. Los Palos Grandes'
  , 'Caracas'
  , 'DF'
  , '1081'
  , 'Venezuela'
  , '(2) 283-2951'
  , '(2) 283-3397'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'HANAR'
  , 'Hanari Carnes'
  , 'Mario Pontes'
  , 'Accounting Manager'
  , 'Rua do Pa?o, 67'
  , 'Rio de Janeiro'
  , 'RJ'
  , '05454-876'
  , 'Brazil'
  , '(21) 555-0091'
  , '(21) 555-8765'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'HILAA'
  , 'HILARIóN-Abastos'
  , 'Carlos Hernández'
  , 'Sales Representative'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
  , '(5) 555-1340'
  , '(5) 555-1948'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'HUNGC'
  , 'Hungry Coyote Import Store'
  , 'Yoshi Latimer'
  , 'Sales Representative'
  , 'City Center Plaza
516 Main St.'
  , 'Elgin'
  , 'OR'
  , '97827'
  , 'USA'
  , '(503) 555-6874'
  , '(503) 555-2376'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'HUNGO'
  , 'Hungry Owl All-Night Grocers'
  , 'Patricia McKenna'
  , 'Sales Associate'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
  , '2967 542'
  , '2967 3333'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'ISLAT'
  , 'Island Trading'
  , 'Helen Bennett'
  , 'Marketing Manager'
  , 'Garden House
Crowther Way'
  , 'Cowes'
  , 'Isle of Wight'
  , 'PO31 7PJ'
  , 'UK'
  , '(198) 555-8888'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'KOENE'
  , 'K?niglich Essen'
  , 'Philip Cramer'
  , 'Sales Associate'
  , 'Maubelstr. 90'
  , 'Brandenburg'
  , NULL
  , '14776'
  , 'Germany'
  , '0555-09876'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'LACOR'
  , 'La corne d''abondance'
  , 'Daniel Tonini'
  , 'Sales Representative'
  , '67, avenue de l''Europe'
  , 'Versailles'
  , NULL
  , '78000'
  , 'France'
  , '30.59.84.10'
  , '30.59.85.11'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'LAMAI'
  , 'La maison d''Asie'
  , 'Annette Roulet'
  , 'Sales Manager'
  , '1 rue Alsace-Lorraine'
  , 'Toulouse'
  , NULL
  , '31000'
  , 'France'
  , '61.77.61.10'
  , '61.77.61.11'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'LAUGB'
  , 'Laughing Bacchus Wine Cellars'
  , 'Yoshi Tannamuri'
  , 'Marketing Assistant'
  , '1900 Oak St.'
  , 'Vancouver'
  , 'BC'
  , 'V3F 2K1'
  , 'Canada'
  , '(604) 555-3392'
  , '(604) 555-7293'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'LAZYK'
  , 'Lazy K Kountry Store'
  , 'John Steel'
  , 'Marketing Manager'
  , '12 Orchestra Terrace'
  , 'Walla Walla'
  , 'WA'
  , '99362'
  , 'USA'
  , '(509) 555-7969'
  , '(509) 555-6221'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'LEHMS'
  , 'Lehmanns Marktstand'
  , 'Renate Messner'
  , 'Sales Representative'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
  , '069-0245984'
  , '069-0245874'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'LETSS'
  , 'Let''s Stop N Shop'
  , 'Jaime Yorres'
  , 'Owner'
  , '87 Polk St.
Suite 5'
  , 'San Francisco'
  , 'CA'
  , '94117'
  , 'USA'
  , '(415) 555-5938'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'LILAS'
  , 'LILA-Supermercado'
  , 'Carlos González'
  , 'Accounting Manager'
  , 'Carrera 52 con Ave. Bolívar #65-98 Llano Largo'
  , 'Barquisimeto'
  , 'Lara'
  , '3508'
  , 'Venezuela'
  , '(9) 331-6954'
  , '(9) 331-7256'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'LINOD'
  , 'LINO-Delicateses'
  , 'Felipe Izquierdo'
  , 'Owner'
  , 'Ave. 5 de Mayo Porlamar'
  , 'I. de Margarita'
  , 'Nueva Esparta'
  , '4980'
  , 'Venezuela'
  , '(8) 34-56-12'
  , '(8) 34-93-93'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'LONEP'
  , 'Lonesome Pine Restaurant'
  , 'Fran Wilson'
  , 'Sales Manager'
  , '89 Chiaroscuro Rd.'
  , 'Portland'
  , 'OR'
  , '97219'
  , 'USA'
  , '(503) 555-9573'
  , '(503) 555-9646'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'MAGAA'
  , 'Magazzini Alimentari Riuniti'
  , 'Giovanni Rovelli'
  , 'Marketing Manager'
  , 'Via Ludovico il Moro 22'
  , 'Bergamo'
  , NULL
  , '24100'
  , 'Italy'
  , '035-640230'
  , '035-640231'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'MAISD'
  , 'Maison Dewey'
  , 'Catherine Dewey'
  , 'Sales Agent'
  , 'Rue Joseph-Bens 532'
  , 'Bruxelles'
  , NULL
  , 'B-1180'
  , 'Belgium'
  , '(02) 201 24 67'
  , '(02) 201 24 68'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'MEREP'
  , 'Mère Paillarde'
  , 'Jean Fresnière'
  , 'Marketing Assistant'
  , '43 rue St. Laurent'
  , 'Montréal'
  , 'Québec'
  , 'H1J 1C3'
  , 'Canada'
  , '(514) 555-8054'
  , '(514) 555-8055'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'MORGK'
  , 'Morgenstern Gesundkost'
  , 'Alexander Feuer'
  , 'Marketing Assistant'
  , 'Heerstr. 22'
  , 'Leipzig'
  , NULL
  , '04179'
  , 'Germany'
  , '0342-023176'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'NORTS'
  , 'North/South'
  , 'Simon Crowther'
  , 'Sales Associate'
  , 'South House
300 Queensbridge'
  , 'London'
  , NULL
  , 'SW7 1RZ'
  , 'UK'
  , '(171) 555-7733'
  , '(171) 555-2530'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'OCEAN'
  , 'Océano Atlántico Ltda.'
  , 'Yvonne Moncada'
  , 'Sales Agent'
  , 'Ing. Gustavo Moncada 8585
Piso 20-A'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
  , '(1) 135-5333'
  , '(1) 135-5535'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'OLDWO'
  , 'Old World Delicatessen'
  , 'Rene Phillips'
  , 'Sales Representative'
  , '2743 Bering St.'
  , 'Anchorage'
  , 'AK'
  , '99508'
  , 'USA'
  , '(907) 555-7584'
  , '(907) 555-2880'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'OTTIK'
  , 'Ottilies K?seladen'
  , 'Henriette Pfalzheim'
  , 'Owner'
  , 'Mehrheimerstr. 369'
  , 'K?ln'
  , NULL
  , '50739'
  , 'Germany'
  , '0221-0644327'
  , '0221-0765721'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'PARIS'
  , 'Paris spécialités'
  , 'Marie Bertrand'
  , 'Owner'
  , '265, boulevard Charonne'
  , 'Paris'
  , NULL
  , '75012'
  , 'France'
  , '(1) 42.34.22.66'
  , '(1) 42.34.22.77'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'PERIC'
  , 'Pericles Comidas clásicas'
  , 'Guillermo Fernández'
  , 'Sales Representative'
  , 'Calle Dr. Jorge Cash 321'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
  , '(5) 552-3745'
  , '(5) 545-3745'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'PICCO'
  , 'Piccolo und mehr'
  , 'Georg Pipps'
  , 'Sales Manager'
  , 'Geislweg 14'
  , 'Salzburg'
  , NULL
  , '5020'
  , 'Austria'
  , '6562-9722'
  , '6562-9723'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'PRINI'
  , 'Princesa Isabel Vinhos'
  , 'Isabel de Castro'
  , 'Sales Representative'
  , 'Estrada da saúde n. 58'
  , 'Lisboa'
  , NULL
  , '1756'
  , 'Portugal'
  , '(1) 356-5634'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'QUEDE'
  , 'Que Delícia'
  , 'Bernardo Batista'
  , 'Accounting Manager'
  , 'Rua da Panificadora, 12'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-673'
  , 'Brazil'
  , '(21) 555-4252'
  , '(21) 555-4545'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'QUEEN'
  , 'Queen Cozinha'
  , 'Lúcia Carvalho'
  , 'Marketing Assistant'
  , 'Alameda dos Canàrios, 891'
  , 'S?o Paulo'
  , 'SP'
  , '05487-020'
  , 'Brazil'
  , '(11) 555-1189'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'QUICK'
  , 'QUICK-Stop'
  , 'Horst Kloss'
  , 'Accounting Manager'
  , 'Taucherstra?e 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
  , '0372-035188'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'RANCH'
  , 'Rancho grande'
  , 'Sergio Gutiérrez'
  , 'Sales Representative'
  , 'Av. del Libertador 900'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
  , '(1) 123-5555'
  , '(1) 123-5556'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'RATTC'
  , 'Rattlesnake Canyon Grocery'
  , 'Paula Wilson'
  , 'Assistant Sales Representative'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
  , '(505) 555-5939'
  , '(505) 555-3620'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'REGGC'
  , 'Reggiani Caseifici'
  , 'Maurizio Moroni'
  , 'Sales Associate'
  , 'Strada Provinciale 124'
  , 'Reggio Emilia'
  , NULL
  , '42100'
  , 'Italy'
  , '0522-556721'
  , '0522-556722'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'RICAR'
  , 'Ricardo Adocicados'
  , 'Janete Limeira'
  , 'Assistant Sales Agent'
  , 'Av. Copacabana, 267'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-890'
  , 'Brazil'
  , '(21) 555-3412'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'RICSU'
  , 'Richter Supermarkt'
  , 'Michael Holz'
  , 'Sales Manager'
  , 'Grenzacherweg 237'
  , 'Genève'
  , NULL
  , '1203'
  , 'Switzerland'
  , '0897-034214'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'ROMEY'
  , 'Romero y tomillo'
  , 'Alejandra Camino'
  , 'Accounting Manager'
  , 'Gran Vía, 1'
  , 'Madrid'
  , NULL
  , '28001'
  , 'Spain'
  , '(91) 745 6200'
  , '(91) 745 6210'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'SANTG'
  , 'Sant?Gourmet'
  , 'Jonas Bergulfsen'
  , 'Owner'
  , 'Erling Skakkes gate 78'
  , 'Stavern'
  , NULL
  , '4110'
  , 'Norway'
  , '07-98 92 35'
  , '07-98 92 47'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'SAVEA'
  , 'Save-a-lot Markets'
  , 'Jose Pavarotti'
  , 'Sales Representative'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
  , '(208) 555-8097'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'SEVES'
  , 'Seven Seas Imports'
  , 'Hari Kumar'
  , 'Sales Manager'
  , '90 Wadhurst Rd.'
  , 'London'
  , NULL
  , 'OX15 4NB'
  , 'UK'
  , '(171) 555-1717'
  , '(171) 555-5646'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'SIMOB'
  , 'Simons bistro'
  , 'Jytte Petersen'
  , 'Owner'
  , 'Vinb?ltet 34'
  , 'K?benhavn'
  , NULL
  , '1734'
  , 'Denmark'
  , '31 12 34 56'
  , '31 13 35 57'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'SPECD'
  , 'Spécialités du monde'
  , 'Dominique Perrier'
  , 'Marketing Manager'
  , '25, rue Lauriston'
  , 'Paris'
  , NULL
  , '75016'
  , 'France'
  , '(1) 47.55.60.10'
  , '(1) 47.55.60.20'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'SPLIR'
  , 'Split Rail Beer & Ale'
  , 'Art Braunschweiger'
  , 'Sales Manager'
  , 'P.O. Box 555'
  , 'Lander'
  , 'WY'
  , '82520'
  , 'USA'
  , '(307) 555-4680'
  , '(307) 555-6525'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'SUPRD'
  , 'Suprêmes délices'
  , 'Pascale Cartrain'
  , 'Accounting Manager'
  , 'Boulevard Tirou, 255'
  , 'Charleroi'
  , NULL
  , 'B-6000'
  , 'Belgium'
  , '(071) 23 67 22 20'
  , '(071) 23 67 22 21'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'THEBI'
  , 'The Big Cheese'
  , 'Liz Nixon'
  , 'Marketing Manager'
  , '89 Jefferson Way
Suite 2'
  , 'Portland'
  , 'OR'
  , '97201'
  , 'USA'
  , '(503) 555-3612'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'THECR'
  , 'The Cracker Box'
  , 'Liu Wong'
  , 'Marketing Assistant'
  , '55 Grizzly Peak Rd.'
  , 'Butte'
  , 'MT'
  , '59801'
  , 'USA'
  , '(406) 555-5834'
  , '(406) 555-8083'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'TOMSP'
  , 'Toms Spezialit?ten'
  , 'Karin Josephs'
  , 'Marketing Manager'
  , 'Luisenstr. 48'
  , 'Münster'
  , NULL
  , '44087'
  , 'Germany'
  , '0251-031259'
  , '0251-035695'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'TORTU'
  , 'Tortuga Restaurante'
  , 'Miguel Angel Paolino'
  , 'Owner'
  , 'Avda. Azteca 123'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
  , '(5) 555-2933'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'TRADH'
  , 'Tradi??o Hipermercados'
  , 'Anabela Domingues'
  , 'Sales Representative'
  , 'Av. Inês de Castro, 414'
  , 'S?o Paulo'
  , 'SP'
  , '05634-030'
  , 'Brazil'
  , '(11) 555-2167'
  , '(11) 555-2168'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'TRAIH'
  , 'Trail''s Head Gourmet Provisioners'
  , 'Helvetius Nagy'
  , 'Sales Associate'
  , '722 DaVinci Blvd.'
  , 'Kirkland'
  , 'WA'
  , '98034'
  , 'USA'
  , '(206) 555-8257'
  , '(206) 555-2174'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'VAFFE'
  , 'Vaffeljernet'
  , 'Palle Ibsen'
  , 'Sales Manager'
  , 'Smagsl?get 45'
  , '?rhus'
  , NULL
  , '8200'
  , 'Denmark'
  , '86 21 32 43'
  , '86 22 33 44'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'VICTE'
  , 'Victuailles en stock'
  , 'Mary Saveley'
  , 'Sales Agent'
  , '2, rue du Commerce'
  , 'Lyon'
  , NULL
  , '69004'
  , 'France'
  , '78.32.54.86'
  , '78.32.54.87'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'VINET'
  , 'Vins et alcools Chevalier'
  , 'Paul Henriot'
  , 'Accounting Manager'
  , '59 rue de l''Abbaye'
  , 'Reims'
  , NULL
  , '51100'
  , 'France'
  , '26.47.15.10'
  , '26.47.15.11'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'WANDK'
  , 'Die Wandernde Kuh'
  , 'Rita Müller'
  , 'Sales Representative'
  , 'Adenauerallee 900'
  , 'Stuttgart'
  , NULL
  , '70563'
  , 'Germany'
  , '0711-020361'
  , '0711-035428'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'WARTH'
  , 'Wartian Herkku'
  , 'Pirkko Koskitalo'
  , 'Accounting Manager'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
  , '981-443655'
  , '981-443655'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'WELLI'
  , 'Wellington Importadora'
  , 'Paula Parente'
  , 'Sales Manager'
  , 'Rua do Mercado, 12'
  , 'Resende'
  , 'SP'
  , '08737-363'
  , 'Brazil'
  , '(14) 555-8122'
  , NULL
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'WHITC'
  , 'White Clover Markets'
  , 'Karl Jablonski'
  , 'Owner'
  , '305 - 14th Ave. S.
Suite 3B'
  , 'Seattle'
  , 'WA'
  , '98128'
  , 'USA'
  , '(206) 555-4112'
  , '(206) 555-4115'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'WILMK'
  , 'Wilman Kala'
  , 'Matti Karttunen'
  , 'Owner/Marketing Assistant'
  , 'Keskuskatu 45'
  , 'Helsinki'
  , NULL
  , '21240'
  , 'Finland'
  , '90-224 8858'
  , '90-224 8858'
);

INSERT INTO CUSTOMERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
)
VALUES
(
    'WOLZA'
  , 'Wolski  Zajazd'
  , 'Zbyszek Piestrzeniewicz'
  , 'Owner'
  , 'ul. Filtrowa 68'
  , 'Warszawa'
  , NULL
  , '01-012'
  , 'Poland'
  , '(26) 642-7012'
  , '(26) 642-7012'
);

INSERT INTO EMPLOYEES
(
    ID
  , LAST_NAME
  , FIRST_NAME
  , TITLE
  , TITLE_OF_COURTESY
  , BIRTH_DATE
  , HIRE_DATE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , EXTENSION
  , REPORTS_TO
  , NOTES
  , PHOTO_PATH
)
VALUES
(
    1
  , 'Davolio'
  , 'Nancy'
  , 'Sales Representative'
  , 'Ms.'
  , '1948-12-08'
  , '1992-05-01'
  , '507 - 20th Ave. E. Apt. 2A'
  , 'Seattle'
  , 'WA'
  , '98122'
  , 'USA'
  , '(206) 555-9857'
  , '5467'
  , NULL
  , 'Education includes a BA in psychology from Colorado State University in 1970.  She also completed "The Art of the Cold Call."  Nancy is a member of Toastmasters International.'
  , '2'
);

INSERT INTO EMPLOYEES
(
    ID
  , LAST_NAME
  , FIRST_NAME
  , TITLE
  , TITLE_OF_COURTESY
  , BIRTH_DATE
  , HIRE_DATE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , EXTENSION
  , REPORTS_TO
  , NOTES
  , PHOTO_PATH
)
VALUES
(
    2
  , 'Fuller'
  , 'Andrew'
  , 'Vice President, Sales'
  , 'Dr.'
  , '1952-02-19'
  , '1992-08-14'
  , '908 W. Capital Way'
  , 'Tacoma'
  , 'WA'
  , '98401'
  , 'USA'
  , '(206) 555-9482'
  , '3457'
  , NULL
  , 'Andrew received his BTS commercial in 1974 and a Ph.D. in international marketing from the University of Dallas in 1981.  He is fluent in French and Italian and reads German.  He joined the company as a sales representative, was promoted to sales manager in January 1992 and to vice president of sales in March 1993.  Andrew is a member of the Sales Management Roundtable, the Seattle Chamber of Commerce, and the Pacific Rim Importers Association.'
  , NULL
);

INSERT INTO EMPLOYEES
(
    ID
  , LAST_NAME
  , FIRST_NAME
  , TITLE
  , TITLE_OF_COURTESY
  , BIRTH_DATE
  , HIRE_DATE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , EXTENSION
  , REPORTS_TO
  , NOTES
  , PHOTO_PATH
)
VALUES
(
    3
  , 'Leverling'
  , 'Janet'
  , 'Sales Representative'
  , 'Ms.'
  , '1963-08-30'
  , '1992-04-01'
  , '722 Moss Bay Blvd.'
  , 'Kirkland'
  , 'WA'
  , '98033'
  , 'USA'
  , '(206) 555-3412'
  , '3355'
  , NULL
  , 'Janet has a BS degree in chemistry from Boston College (1984).  She has also completed a certificate program in food retailing management.  Janet was hired as a sales associate in 1991 and promoted to sales representative in February 1992.'
  , '2'
);

INSERT INTO EMPLOYEES
(
    ID
  , LAST_NAME
  , FIRST_NAME
  , TITLE
  , TITLE_OF_COURTESY
  , BIRTH_DATE
  , HIRE_DATE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , EXTENSION
  , REPORTS_TO
  , NOTES
  , PHOTO_PATH
)
VALUES
(
    4
  , 'Peacock'
  , 'Margaret'
  , 'Sales Representative'
  , 'Mrs.'
  , '1937-09-19'
  , '1993-05-03'
  , '4110 Old Redmond Rd.'
  , 'Redmond'
  , 'WA'
  , '98052'
  , 'USA'
  , '(206) 555-8122'
  , '5176'
  , NULL
  , 'Margaret holds a BA in English literature from Concordia College (1958) and an MA from the American Institute of Culinary Arts (1966).  She was assigned to the London office temporarily from July through November 1992.'
  , '2'
);

INSERT INTO EMPLOYEES
(
    ID
  , LAST_NAME
  , FIRST_NAME
  , TITLE
  , TITLE_OF_COURTESY
  , BIRTH_DATE
  , HIRE_DATE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , EXTENSION
  , REPORTS_TO
  , NOTES
  , PHOTO_PATH
)
VALUES
(
    5
  , 'Buchanan'
  , 'Steven'
  , 'Sales Manager'
  , 'Mr.'
  , '1955-03-04'
  , '1993-10-17'
  , '14 Garrett Hill'
  , 'London'
  , NULL
  , 'SW1 8JR'
  , 'UK'
  , '(71) 555-4848'
  , '3453'
  , NULL
  , 'Steven Buchanan graduated from St. Andrews University, Scotland, with a BSC degree in 1976.  Upon joining the company as a sales representative in 1992, he spent 6 months in an orientation program at the Seattle office and then returned to his permanent post in London.  He was promoted to sales manager in March 1993.  Mr. Buchanan has completed the courses "Successful Telemarketing" and "International Sales Management."  He is fluent in French.'
  , '2'
);

INSERT INTO EMPLOYEES
(
    ID
  , LAST_NAME
  , FIRST_NAME
  , TITLE
  , TITLE_OF_COURTESY
  , BIRTH_DATE
  , HIRE_DATE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , EXTENSION
  , REPORTS_TO
  , NOTES
  , PHOTO_PATH
)
VALUES
(
    6
  , 'Suyama'
  , 'Michael'
  , 'Sales Representative'
  , 'Mr.'
  , '1963-07-02'
  , '1993-10-17'
  , 'Coventry House Miner Rd.'
  , 'London'
  , NULL
  , 'EC2 7JR'
  , 'UK'
  , '(71) 555-7773'
  , '428'
  , NULL
  , 'Michael is a graduate of Sussex University (MA, economics, 1983) and the University of California at Los Angeles (MBA, marketing, 1986).  He has also taken the courses "Multi-Cultural Selling" and "Time Management for the Sales Professional."  He is fluent in Japanese and can read and write French, Portuguese, and Spanish.'
  , '5'
);

INSERT INTO EMPLOYEES
(
    ID
  , LAST_NAME
  , FIRST_NAME
  , TITLE
  , TITLE_OF_COURTESY
  , BIRTH_DATE
  , HIRE_DATE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , EXTENSION
  , REPORTS_TO
  , NOTES
  , PHOTO_PATH
)
VALUES
(
    7
  , 'King'
  , 'Robert'
  , 'Sales Representative'
  , 'Mr.'
  , '1960-05-29'
  , '1994-01-02'
  , 'Edgeham Hollow Winchester Way'
  , 'London'
  , NULL
  , 'RG1 9SP'
  , 'UK'
  , '(71) 555-5598'
  , '465'
  , NULL
  , 'Robert King served in the Peace Corps and traveled extensively before completing his degree in English at the University of Michigan in 1992, the year he joined the company.  After completing a course entitled "Selling in Europe," he was transferred to the London office in March 1993.'
  , '5'
);

INSERT INTO EMPLOYEES
(
    ID
  , LAST_NAME
  , FIRST_NAME
  , TITLE
  , TITLE_OF_COURTESY
  , BIRTH_DATE
  , HIRE_DATE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , EXTENSION
  , REPORTS_TO
  , NOTES
  , PHOTO_PATH
)
VALUES
(
    8
  , 'Callahan'
  , 'Laura'
  , 'Inside Sales Coordinator'
  , 'Ms.'
  , '1958-01-09'
  , '1994-03-05'
  , '4726 - 11th Ave. N.E.'
  , 'Seattle'
  , 'WA'
  , '98105'
  , 'USA'
  , '(206) 555-1189'
  , '2344'
  , NULL
  , 'Laura received a BA in psychology from the University of Washington.  She has also completed a course in business French.  She reads and writes French.'
  , '2'
);

INSERT INTO EMPLOYEES
(
    ID
  , LAST_NAME
  , FIRST_NAME
  , TITLE
  , TITLE_OF_COURTESY
  , BIRTH_DATE
  , HIRE_DATE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , EXTENSION
  , REPORTS_TO
  , NOTES
  , PHOTO_PATH
)
VALUES
(
    9
  , 'Dodsworth'
  , 'Anne'
  , 'Sales Representative'
  , 'Ms.'
  , '1966-01-27'
  , '1994-11-15'
  , '7 Houndstooth Rd.'
  , 'London'
  , NULL
  , 'WG2 7LT'
  , 'UK'
  , '(71) 555-4444'
  , '452'
  , NULL
  , 'Anne has a BA degree in English from St. Lawrence College.  She is fluent in French and German.'
  , '5'
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    1
  , 'Exotic Liquids'
  , 'Charlotte Cooper'
  , 'Purchasing Manager'
  , '49 Gilbert St.'
  , 'London'
  , NULL
  , 'EC1 4SD'
  , 'UK'
  , '(171) 555-2222'
  , NULL
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    2
  , 'New Orleans Cajun Delights'
  , 'Shelley Burke'
  , 'Order Administrator'
  , 'P.O. Box 78934'
  , 'New Orleans'
  , 'LA'
  , '70117'
  , 'USA'
  , '(100) 555-4822'
  , NULL
  , '#CAJUN.HTM#'
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    3
  , 'Grandma Kelly''s Homestead'
  , 'Regina Murphy'
  , 'Sales Representative'
  , '707 Oxford Rd.'
  , 'Ann Arbor'
  , 'MI'
  , '48104'
  , 'USA'
  , '(313) 555-5735'
  , '(313) 555-3349'
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    4
  , 'Tokyo Traders'
  , 'Yoshi Nagase'
  , 'Marketing Manager'
  , '9-8 Sekimai
Musashino-shi'
  , 'Tokyo'
  , NULL
  , '100'
  , 'Japan'
  , '(03) 3555-5011'
  , NULL
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    5
  , 'Cooperativa de Quesos ''Las Cabras'''
  , 'Antonio del Valle Saavedra '
  , 'Export Administrator'
  , 'Calle del Rosal 4'
  , 'Oviedo'
  , 'Asturias'
  , '33007'
  , 'Spain'
  , '(98) 598 76 54'
  , NULL
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    6
  , 'Mayumi''s'
  , 'Mayumi Ohno'
  , 'Marketing Representative'
  , '92 Setsuko
Chuo-ku'
  , 'Osaka'
  , NULL
  , '545'
  , 'Japan'
  , '(06) 431-7877'
  , NULL
  , 'Mayumi''s (on the World Wide Web)#http://www.microsoft.com/accessdev/sampleapps/mayumi.htm#'
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    7
  , 'Pavlova, Ltd.'
  , 'Ian Devling'
  , 'Marketing Manager'
  , '74 Rose St.
Moonie Ponds'
  , 'Melbourne'
  , 'Victoria'
  , '3058'
  , 'Australia'
  , '(03) 444-2343'
  , '(03) 444-6588'
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    8
  , 'Specialty Biscuits, Ltd.'
  , 'Peter Wilson'
  , 'Sales Representative'
  , '29 King''s Way'
  , 'Manchester'
  , NULL
  , 'M14 GSD'
  , 'UK'
  , '(161) 555-4448'
  , NULL
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    9
  , 'PB Knäckebröd AB'
  , 'Lars Peterson'
  , 'Sales Agent'
  , 'Kaloadagatan 13'
  , 'Göteborg'
  , NULL
  , 'S-345 67'
  , 'Sweden '
  , '031-987 65 43'
  , '031-987 65 91'
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    10
  , 'Refrescos Americanas LTDA'
  , 'Carlos Diaz'
  , 'Marketing Manager'
  , 'Av. das Americanas 12.890'
  , 'São Paulo'
  , NULL
  , '5442'
  , 'Brazil'
  , '(11) 555 4640'
  , NULL
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    11
  , 'Heli Süßwaren GmbH & Co. KG'
  , 'Petra Winkler'
  , 'Sales Manager'
  , 'Tiergartenstraße 5'
  , 'Berlin'
  , NULL
  , '10785'
  , 'Germany'
  , '(010) 9984510'
  , NULL
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    12
  , 'Plutzer Lebensmittelgroßmärkte AG'
  , 'Martin Bein'
  , 'International Marketing Mgr.'
  , 'Bogenallee 51'
  , 'Frankfurt'
  , NULL
  , '60439'
  , 'Germany'
  , '(069) 992755'
  , NULL
  , 'Plutzer (on the World Wide Web)#http://www.microsoft.com/accessdev/sampleapps/plutzer.htm#'
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    13
  , 'Nord-Ost-Fisch Handelsgesellschaft mbH'
  , 'Sven Petersen'
  , 'Coordinator Foreign Markets'
  , 'Frahmredder 112a'
  , 'Cuxhaven'
  , NULL
  , '27478'
  , 'Germany'
  , '(04721) 8713'
  , '(04721) 8714'
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    14
  , 'Formaggi Fortini s.r.l.'
  , 'Elio Rossi'
  , 'Sales Representative'
  , 'Viale Dante, 75'
  , 'Ravenna'
  , NULL
  , '48100'
  , 'Italy'
  , '(0544) 60323'
  , '(0544) 60603'
  , '#FORMAGGI.HTM#'
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    15
  , 'Norske Meierier'
  , 'Beate Vileid'
  , 'Marketing Manager'
  , 'Hatlevegen 5'
  , 'Sandvika'
  , NULL
  , '1320'
  , 'Norway'
  , '(0)2-953010'
  , NULL
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    16
  , 'Bigfoot Breweries'
  , 'Cheryl Saylor'
  , 'Regional Account Rep.'
  , '3400 - 8th Avenue
Suite 210'
  , 'Bend'
  , 'OR'
  , '97101'
  , 'USA'
  , '(503) 555-9931'
  , NULL
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    17
  , 'Svensk Sjöföda AB'
  , 'Michael Björn'
  , 'Sales Representative'
  , 'Brovallavägen 231'
  , 'Stockholm'
  , NULL
  , 'S-123 45'
  , 'Sweden'
  , '08-123 45 67'
  , NULL
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    18
  , 'Aux joyeux ecclésiastiques'
  , 'Guylène Nodier'
  , 'Sales Manager'
  , '203, Rue des Francs-Bourgeois'
  , 'Paris'
  , NULL
  , '75004'
  , 'France'
  , '(1) 03.83.00.68'
  , '(1) 03.83.00.62'
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    19
  , 'New England Seafood Cannery'
  , 'Robb Merchant'
  , 'Wholesale Account Agent'
  , 'Order Processing Dept.
2100 Paul Revere Blvd.'
  , 'Boston'
  , 'MA'
  , '02134'
  , 'USA'
  , '(617) 555-3267'
  , '(617) 555-3389'
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    20
  , 'Leka Trading'
  , 'Chandra Leka'
  , 'Owner'
  , '471 Serangoon Loop, Suite #402'
  , 'Singapore'
  , NULL
  , '0512'
  , 'Singapore'
  , '555-8787'
  , NULL
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    21
  , 'Lyngbysild'
  , 'Niels Petersen'
  , 'Sales Manager'
  , 'Lyngbysild
Fiskebakken 10'
  , 'Lyngby'
  , NULL
  , '2800'
  , 'Denmark'
  , '43844108'
  , '43844115'
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    22
  , 'Zaanse Snoepfabriek'
  , 'Dirk Luchte'
  , 'Accounting Manager'
  , 'Verkoop
Rijnweg 22'
  , 'Zaandam'
  , NULL
  , '9999 ZZ'
  , 'Netherlands'
  , '(12345) 1212'
  , '(12345) 1210'
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    23
  , 'Karkki Oy'
  , 'Anne Heikkonen'
  , 'Product Manager'
  , 'Valtakatu 12'
  , 'Lappeenranta'
  , NULL
  , '53120'
  , 'Finland'
  , '(953) 10956'
  , NULL
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    24
  , 'G''day, Mate'
  , 'Wendy Mackenzie'
  , 'Sales Representative'
  , '170 Prince Edward Parade
Hunter''s Hill'
  , 'Sydney'
  , 'NSW'
  , '2042'
  , 'Australia'
  , '(02) 555-5914'
  , '(02) 555-4873'
  , 'G''day Mate (on the World Wide Web)#http://www.microsoft.com/accessdev/sampleapps/gdaymate.htm#'
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    25
  , 'Ma Maison'
  , 'Jean-Guy Lauzon'
  , 'Marketing Manager'
  , '2960 Rue St. Laurent'
  , 'Montréal'
  , 'Québec'
  , 'H1J 1C3'
  , 'Canada'
  , '(514) 555-9022'
  , NULL
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    26
  , 'Pasta Buttini s.r.l.'
  , 'Giovanni Giudici'
  , 'Order Administrator'
  , 'Via dei Gelsomini, 153'
  , 'Salerno'
  , NULL
  , '84100'
  , 'Italy'
  , '(089) 6547665'
  , '(089) 6547667'
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    27
  , 'Escargots Nouveaux'
  , 'Marie Delamare'
  , 'Sales Manager'
  , '22, rue H. Voiron'
  , 'Montceau'
  , NULL
  , '71300'
  , 'France'
  , '85.57.00.07'
  , NULL
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    28
  , 'Gai pâturage'
  , 'Eliane Noz'
  , 'Sales Representative'
  , 'Bat. B
3, rue des Alpes'
  , 'Annecy'
  , NULL
  , '74000'
  , 'France'
  , '38.76.98.06'
  , '38.76.98.58'
  , ''
);

INSERT INTO SUPPLIERS
(
    ID
  , COMPANY_NAME
  , CONTACT_NAME
  , CONTACT_TITLE
  , ADDRESS
  , CITY
  , REGION
  , POSTAL_CODE
  , COUNTRY
  , PHONE
  , FAX
  , HOME_PAGE
)
VALUES
(
    29
  , 'Forêts d''érables'
  , 'Chantal Goulet'
  , 'Accounting Manager'
  , '148 rue Chasseur'
  , 'Ste-Hyacinthe'
  , 'Québec'
  , 'J2S 7S8'
  , 'Canada'
  , '(514) 555-2955'
  , '(514) 555-2921'
  , ''
);

INSERT INTO SHIPPERS
(ID, COMPANY_NAME, PHONE)
VALUES
(1, 'Speedy Express', '(503) 555-9831');

INSERT INTO SHIPPERS
(ID, COMPANY_NAME, PHONE)
VALUES
(2, 'United Package', '(503) 555-3199');

INSERT INTO SHIPPERS
(ID, COMPANY_NAME, PHONE)
VALUES
(3, 'Federal Shipping', '(503) 555-9931');

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    1
  , 'Chai'
  , 1
  , 1
  , '10 boxes x 20 bags'
  , 18.00
  , 39
  , 0
  , 10
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    2
  , 'Chang'
  , 1
  , 1
  , '24 - 12 oz bottles'
  , 19.00
  , 17
  , 40
  , 25
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    3
  , 'Aniseed Syrup'
  , 1
  , 2
  , '12 - 550 ml bottles'
  , 10.00
  , 13
  , 70
  , 25
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    4
  , 'Chef Anton''s Cajun Seasoning'
  , 2
  , 2
  , '48 - 6 oz jars'
  , 22.00
  , 53
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    5
  , 'Chef Anton''s Gumbo Mix'
  , 2
  , 2
  , '36 boxes'
  , 21.35
  , 0
  , 0
  , 0
  , 1
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    6
  , 'Grandma''s Boysenberry Spread'
  , 3
  , 2
  , '12 - 8 oz jars'
  , 25.00
  , 120
  , 0
  , 25
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    7
  , 'Uncle Bob''s Organic Dried Pears'
  , 3
  , 7
  , '12 - 1 lb pkgs.'
  , 30.00
  , 15
  , 0
  , 10
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    8
  , 'Northwoods Cranberry Sauce'
  , 3
  , 2
  , '12 - 12 oz jars'
  , 40.00
  , 6
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    9
  , 'Mishi Kobe Niku'
  , 4
  , 6
  , '18 - 500 g pkgs.'
  , 97.00
  , 29
  , 0
  , 0
  , 1
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    10
  , 'Ikura'
  , 4
  , 8
  , '12 - 200 ml jars'
  , 31.00
  , 31
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    11
  , 'Queso Cabrales'
  , 5
  , 4
  , '1 kg pkg.'
  , 21.00
  , 22
  , 30
  , 30
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    12
  , 'Queso Manchego La Pastora'
  , 5
  , 4
  , '10 - 500 g pkgs.'
  , 38.00
  , 86
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    13
  , 'Konbu'
  , 6
  , 8
  , '2 kg box'
  , 6.00
  , 24
  , 0
  , 5
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    14
  , 'Tofu'
  , 6
  , 7
  , '40 - 100 g pkgs.'
  , 23.25
  , 35
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    15
  , 'Genen Shouyu'
  , 6
  , 2
  , '24 - 250 ml bottles'
  , 15.50
  , 39
  , 0
  , 5
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    16
  , 'Pavlova'
  , 7
  , 3
  , '32 - 500 g boxes'
  , 17.45
  , 29
  , 0
  , 10
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    17
  , 'Alice Mutton'
  , 7
  , 6
  , '20 - 1 kg tins'
  , 39.00
  , 0
  , 0
  , 0
  , 1
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    18
  , 'Carnarvon Tigers'
  , 7
  , 8
  , '16 kg pkg.'
  , 62.50
  , 42
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    19
  , 'Teatime Chocolate Biscuits'
  , 8
  , 3
  , '10 boxes x 12 pieces'
  , 9.20
  , 25
  , 0
  , 5
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    20
  , 'Sir Rodney''s Marmalade'
  , 8
  , 3
  , '30 gift boxes'
  , 81.00
  , 40
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    21
  , 'Sir Rodney''s Scones'
  , 8
  , 3
  , '24 pkgs. x 4 pieces'
  , 10.00
  , 3
  , 40
  , 5
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    22
  , 'Gustaf''s Knäckebröd'
  , 9
  , 5
  , '24 - 500 g pkgs.'
  , 21.00
  , 104
  , 0
  , 25
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    23
  , 'Tunnbröd'
  , 9
  , 5
  , '12 - 250 g pkgs.'
  , 9.00
  , 61
  , 0
  , 25
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    24
  , 'Guaran?Fantástica'
  , 10
  , 1
  , '12 - 355 ml cans'
  , 4.50
  , 20
  , 0
  , 0
  , 1
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    25
  , 'NuNuCa Nu?Nougat-Creme'
  , 11
  , 3
  , '20 - 450 g glasses'
  , 14.00
  , 76
  , 0
  , 30
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    26
  , 'Gumbär Gummibärchen'
  , 11
  , 3
  , '100 - 250 g bags'
  , 31.23
  , 15
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    27
  , 'Schoggi Schokolade'
  , 11
  , 3
  , '100 - 100 g pieces'
  , 43.90
  , 49
  , 0
  , 30
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    28
  , 'Rössle Sauerkraut'
  , 12
  , 7
  , '25 - 825 g cans'
  , 45.60
  , 26
  , 0
  , 0
  , 1
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    29
  , 'Thüringer Rostbratwurst'
  , 12
  , 6
  , '50 bags x 30 sausgs.'
  , 123.79
  , 0
  , 0
  , 0
  , 1
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    30
  , 'Nord-Ost Matjeshering'
  , 13
  , 8
  , '10 - 200 g glasses'
  , 25.89
  , 10
  , 0
  , 15
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    31
  , 'Gorgonzola Telino'
  , 14
  , 4
  , '12 - 100 g pkgs'
  , 12.50
  , 0
  , 70
  , 20
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    32
  , 'Mascarpone Fabioli'
  , 14
  , 4
  , '24 - 200 g pkgs.'
  , 32.00
  , 9
  , 40
  , 25
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    33
  , 'Geitost'
  , 15
  , 4
  , '500 g'
  , 2.50
  , 112
  , 0
  , 20
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    34
  , 'Sasquatch Ale'
  , 16
  , 1
  , '24 - 12 oz bottles'
  , 14.00
  , 111
  , 0
  , 15
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    35
  , 'Steeleye Stout'
  , 16
  , 1
  , '24 - 12 oz bottles'
  , 18.00
  , 20
  , 0
  , 15
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    36
  , 'Inlagd Sill'
  , 17
  , 8
  , '24 - 250 g  jars'
  , 19.00
  , 112
  , 0
  , 20
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    37
  , 'Gravad lax'
  , 17
  , 8
  , '12 - 500 g pkgs.'
  , 26.00
  , 11
  , 50
  , 25
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    38
  , 'Côte de Blaye'
  , 18
  , 1
  , '12 - 75 cl bottles'
  , 263.50
  , 17
  , 0
  , 15
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    39
  , 'Chartreuse verte'
  , 18
  , 1
  , '750 cc per bottle'
  , 18.00
  , 69
  , 0
  , 5
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    40
  , 'Boston Crab Meat'
  , 19
  , 8
  , '24 - 4 oz tins'
  , 18.40
  , 123
  , 0
  , 30
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    41
  , 'Jack''s New England Clam Chowder'
  , 19
  , 8
  , '12 - 12 oz cans'
  , 9.65
  , 85
  , 0
  , 10
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    42
  , 'Singaporean Hokkien Fried Mee'
  , 20
  , 5
  , '32 - 1 kg pkgs.'
  , 14.00
  , 26
  , 0
  , 0
  , 1
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    43
  , 'Ipoh Coffee'
  , 20
  , 1
  , '16 - 500 g tins'
  , 46.00
  , 17
  , 10
  , 25
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    44
  , 'Gula Malacca'
  , 20
  , 2
  , '20 - 2 kg bags'
  , 19.45
  , 27
  , 0
  , 15
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    45
  , 'Røgede sild'
  , 21
  , 8
  , '1k pkg.'
  , 9.50
  , 5
  , 70
  , 15
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    46
  , 'Spegesild'
  , 21
  , 8
  , '4 - 450 g glasses'
  , 12.00
  , 95
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    47
  , 'Zaanse koeken'
  , 22
  , 3
  , '10 - 4 oz boxes'
  , 9.50
  , 36
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    48
  , 'Chocolade'
  , 22
  , 3
  , '10 pkgs.'
  , 12.75
  , 15
  , 70
  , 25
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    49
  , 'Maxilaku'
  , 23
  , 3
  , '24 - 50 g pkgs.'
  , 20.00
  , 10
  , 60
  , 15
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    50
  , 'Valkoinen suklaa'
  , 23
  , 3
  , '12 - 100 g bars'
  , 16.25
  , 65
  , 0
  , 30
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    51
  , 'Manjimup Dried Apples'
  , 24
  , 7
  , '50 - 300 g pkgs.'
  , 53.00
  , 20
  , 0
  , 10
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    52
  , 'Filo Mix'
  , 24
  , 5
  , '16 - 2 kg boxes'
  , 7.00
  , 38
  , 0
  , 25
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    53
  , 'Perth Pasties'
  , 24
  , 6
  , '48 pieces'
  , 32.80
  , 0
  , 0
  , 0
  , 1
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    54
  , 'Tourtière'
  , 25
  , 6
  , '16 pies'
  , 7.45
  , 21
  , 0
  , 10
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    55
  , 'Pât?chinois'
  , 25
  , 6
  , '24 boxes x 2 pies'
  , 24.00
  , 115
  , 0
  , 20
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    56
  , 'Gnocchi di nonna Alice'
  , 26
  , 5
  , '24 - 250 g pkgs.'
  , 38.00
  , 21
  , 10
  , 30
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    57
  , 'Ravioli Angelo'
  , 26
  , 5
  , '24 - 250 g pkgs.'
  , 19.50
  , 36
  , 0
  , 20
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    58
  , 'Escargots de Bourgogne'
  , 27
  , 8
  , '24 pieces'
  , 13.25
  , 62
  , 0
  , 20
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    59
  , 'Raclette Courdavault'
  , 28
  , 4
  , '5 kg pkg.'
  , 55.00
  , 79
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    60
  , 'Camembert Pierrot'
  , 28
  , 4
  , '15 - 300 g rounds'
  , 34.00
  , 19
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    61
  , 'Sirop d''érable'
  , 29
  , 2
  , '24 - 500 ml bottles'
  , 28.50
  , 113
  , 0
  , 25
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    62
  , 'Tarte au sucre'
  , 29
  , 3
  , '48 pies'
  , 49.30
  , 17
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    63
  , 'Vegie-spread'
  , 7
  , 2
  , '15 - 625 g jars'
  , 43.90
  , 24
  , 0
  , 5
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    64
  , 'Wimmers gute Semmelknödel'
  , 12
  , 5
  , '20 bags x 4 pieces'
  , 33.25
  , 22
  , 80
  , 30
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    65
  , 'Louisiana Fiery Hot Pepper Sauce'
  , 2
  , 2
  , '32 - 8 oz bottles'
  , 21.05
  , 76
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    66
  , 'Louisiana Hot Spiced Okra'
  , 2
  , 2
  , '24 - 8 oz jars'
  , 17.00
  , 4
  , 100
  , 20
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    67
  , 'Laughing Lumberjack Lager'
  , 16
  , 1
  , '24 - 12 oz bottles'
  , 14.00
  , 52
  , 0
  , 10
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    68
  , 'Scottish Longbreads'
  , 8
  , 3
  , '10 boxes x 8 pieces'
  , 12.50
  , 6
  , 10
  , 15
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    69
  , 'Gudbrandsdalsost'
  , 15
  , 4
  , '10 kg pkg.'
  , 36.00
  , 26
  , 0
  , 15
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    70
  , 'Outback Lager'
  , 7
  , 1
  , '24 - 355 ml bottles'
  , 15.00
  , 15
  , 10
  , 30
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    71
  , 'Fløtemysost'
  , 15
  , 4
  , '10 - 500 g pkgs.'
  , 21.50
  , 26
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    72
  , 'Mozzarella di Giovanni'
  , 14
  , 4
  , '24 - 200 g pkgs.'
  , 34.80
  , 14
  , 0
  , 0
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    73
  , 'Röd Kaviar'
  , 17
  , 8
  , '24 - 150 g jars'
  , 15.00
  , 101
  , 0
  , 5
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    74
  , 'Longlife Tofu'
  , 4
  , 7
  , '5 kg pkg.'
  , 10.00
  , 4
  , 20
  , 5
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    75
  , 'Rhönbräu Klosterbier'
  , 12
  , 1
  , '24 - 0.5 l bottles'
  , 7.75
  , 125
  , 0
  , 25
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    76
  , 'Lakkalikööri'
  , 23
  , 1
  , '500 ml'
  , 18.00
  , 57
  , 0
  , 20
  , 0
);

INSERT INTO PRODUCTS
(
    ID
  , PRODUCT_NAME
  , SUPPLIER_ID
  , CATEGORY_ID
  , QUANTITY_PER_UNIT
  , UNIT_PRICE
  , UNITS_IN_STOCK
  , UNITS_ON_ORDER
  , REORDER_LEVEL
  , DISCONTINUED
)
VALUES
(
    77
  , 'Original Frankfurter grüne Soße'
  , 12
  , 2
  , '12 boxes'
  , 13.00
  , 32
  , 0
  , 15
  , 0
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10248
  , 'VINET'
  , 5
  , '1994-08-04'
  , '1994-09-01'
  , '1994-08-16'
  , 3
  , 32.38
  , 'Vins et alcools Chevalier'
  , '59 rue de l''Abbaye'
  , 'Reims'
  , NULL
  , '51100'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10249
  , 'TOMSP'
  , 6
  , '1994-08-05'
  , '1994-09-16'
  , '1994-08-10'
  , 1
  , 11.61
  , 'Toms Spezialitäten'
  , 'Luisenstr. 48'
  , 'Münster'
  , NULL
  , '44087'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10250
  , 'HANAR'
  , 4
  , '1994-08-08'
  , '1994-09-05'
  , '1994-08-12'
  , 2
  , 65.83
  , 'Hanari Carnes'
  , 'Rua do Paço, 67'
  , 'Rio de Janeiro'
  , 'RJ'
  , '05454-876'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10251
  , 'VICTE'
  , 3
  , '1994-08-08'
  , '1994-09-05'
  , '1994-08-15'
  , 1
  , 41.34
  , 'Victuailles en stock'
  , '2, rue du Commerce'
  , 'Lyon'
  , NULL
  , '69004'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10252
  , 'SUPRD'
  , 4
  , '1994-08-09'
  , '1994-09-06'
  , '1994-08-11'
  , 2
  , 51.30
  , 'Suprêmes délices'
  , 'Boulevard Tirou, 255'
  , 'Charleroi'
  , NULL
  , 'B-6000'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10253
  , 'HANAR'
  , 3
  , '1994-08-10'
  , '1994-08-24'
  , '1994-08-16'
  , 2
  , 58.17
  , 'Hanari Carnes'
  , 'Rua do Paço, 67'
  , 'Rio de Janeiro'
  , 'RJ'
  , '05454-876'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10254
  , 'CHOPS'
  , 5
  , '1994-08-11'
  , '1994-09-08'
  , '1994-08-23'
  , 2
  , 22.98
  , 'Chop-suey Chinese'
  , 'Hauptstr. 31'
  , 'Bern'
  , NULL
  , '3012'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10255
  , 'RICSU'
  , 9
  , '1994-08-12'
  , '1994-09-09'
  , '1994-08-15'
  , 3
  , 148.33
  , 'Richter Supermarkt'
  , 'Starenweg 5'
  , 'Genève'
  , NULL
  , '1204'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10256
  , 'WELLI'
  , 3
  , '1994-08-15'
  , '1994-09-12'
  , '1994-08-17'
  , 2
  , 13.97
  , 'Wellington Importadora'
  , 'Rua do Mercado, 12'
  , 'Resende'
  , 'SP'
  , '08737-363'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10257
  , 'HILAA'
  , 4
  , '1994-08-16'
  , '1994-09-13'
  , '1994-08-22'
  , 3
  , 81.91
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10258
  , 'ERNSH'
  , 1
  , '1994-08-17'
  , '1994-09-14'
  , '1994-08-23'
  , 1
  , 140.51
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10259
  , 'CENTC'
  , 4
  , '1994-08-18'
  , '1994-09-15'
  , '1994-08-25'
  , 3
  , 3.25
  , 'Centro comercial Moctezuma'
  , 'Sierras de Granada 9993'
  , 'México D.F.'
  , NULL
  , '05022'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10260
  , 'OTTIK'
  , 4
  , '1994-08-19'
  , '1994-09-16'
  , '1994-08-29'
  , 1
  , 55.09
  , 'Ottilies Käseladen'
  , 'Mehrheimerstr. 369'
  , 'Köln'
  , NULL
  , '50739'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10261
  , 'QUEDE'
  , 4
  , '1994-08-19'
  , '1994-09-16'
  , '1994-08-30'
  , 2
  , 3.05
  , 'Que Delícia'
  , 'Rua da Panificadora, 12'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-673'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10262
  , 'RATTC'
  , 8
  , '1994-08-22'
  , '1994-09-19'
  , '1994-08-25'
  , 3
  , 48.29
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10263
  , 'ERNSH'
  , 9
  , '1994-08-23'
  , '1994-09-20'
  , '1994-08-31'
  , 3
  , 146.06
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10264
  , 'FOLKO'
  , 6
  , '1994-08-24'
  , '1994-09-21'
  , '1994-09-23'
  , 3
  , 3.67
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10265
  , 'BLONP'
  , 2
  , '1994-08-25'
  , '1994-09-22'
  , '1994-09-12'
  , 1
  , 55.28
  , 'Blondel père et fils'
  , '24, place Kléber'
  , 'Strasbourg'
  , NULL
  , '67000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10266
  , 'WARTH'
  , 3
  , '1994-08-26'
  , '1994-10-07'
  , '1994-08-31'
  , 3
  , 25.73
  , 'Wartian Herkku'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10267
  , 'FRANK'
  , 4
  , '1994-08-29'
  , '1994-09-26'
  , '1994-09-06'
  , 1
  , 208.58
  , 'Frankenversand'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10268
  , 'GROSR'
  , 8
  , '1994-08-30'
  , '1994-09-27'
  , '1994-09-02'
  , 3
  , 66.29
  , 'GROSELLA-Restaurante'
  , '5?Ave. Los Palos Grandes'
  , 'Caracas'
  , 'DF'
  , '1081'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10269
  , 'WHITC'
  , 5
  , '1994-08-31'
  , '1994-09-14'
  , '1994-09-09'
  , 1
  , 4.56
  , 'White Clover Markets'
  , '1029 - 12th Ave. S.'
  , 'Seattle'
  , 'WA'
  , '98124'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10270
  , 'WARTH'
  , 1
  , '1994-09-01'
  , '1994-09-29'
  , '1994-09-02'
  , 1
  , 136.54
  , 'Wartian Herkku'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10271
  , 'SPLIR'
  , 6
  , '1994-09-01'
  , '1994-09-29'
  , '1994-09-30'
  , 2
  , 4.54
  , 'Split Rail Beer & Ale'
  , 'P.O. Box 555'
  , 'Lander'
  , 'WY'
  , '82520'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10272
  , 'RATTC'
  , 6
  , '1994-09-02'
  , '1994-09-30'
  , '1994-09-06'
  , 2
  , 98.03
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10273
  , 'QUICK'
  , 3
  , '1994-09-05'
  , '1994-10-03'
  , '1994-09-12'
  , 3
  , 76.07
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10274
  , 'VINET'
  , 6
  , '1994-09-06'
  , '1994-10-04'
  , '1994-09-16'
  , 1
  , 6.01
  , 'Vins et alcools Chevalier'
  , '59 rue de l''Abbaye'
  , 'Reims'
  , NULL
  , '51100'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10275
  , 'MAGAA'
  , 1
  , '1994-09-07'
  , '1994-10-05'
  , '1994-09-09'
  , 1
  , 26.93
  , 'Magazzini Alimentari Riuniti'
  , 'Via Ludovico il Moro 22'
  , 'Bergamo'
  , NULL
  , '24100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10276
  , 'TORTU'
  , 8
  , '1994-09-08'
  , '1994-09-22'
  , '1994-09-14'
  , 3
  , 13.84
  , 'Tortuga Restaurante'
  , 'Avda. Azteca 123'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10277
  , 'MORGK'
  , 2
  , '1994-09-09'
  , '1994-10-07'
  , '1994-09-13'
  , 3
  , 125.77
  , 'Morgenstern Gesundkost'
  , 'Heerstr. 22'
  , 'Leipzig'
  , NULL
  , '04179'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10278
  , 'BERGS'
  , 8
  , '1994-09-12'
  , '1994-10-10'
  , '1994-09-16'
  , 2
  , 92.69
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10279
  , 'LEHMS'
  , 8
  , '1994-09-13'
  , '1994-10-11'
  , '1994-09-16'
  , 2
  , 25.83
  , 'Lehmanns Marktstand'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10280
  , 'BERGS'
  , 2
  , '1994-09-14'
  , '1994-10-12'
  , '1994-10-13'
  , 1
  , 8.98
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10281
  , 'ROMEY'
  , 4
  , '1994-09-14'
  , '1994-09-28'
  , '1994-09-21'
  , 1
  , 2.94
  , 'Romero y tomillo'
  , 'Gran Vía, 1'
  , 'Madrid'
  , NULL
  , '28001'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10282
  , 'ROMEY'
  , 4
  , '1994-09-15'
  , '1994-10-13'
  , '1994-09-21'
  , 1
  , 12.69
  , 'Romero y tomillo'
  , 'Gran Vía, 1'
  , 'Madrid'
  , NULL
  , '28001'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10283
  , 'LILAS'
  , 3
  , '1994-09-16'
  , '1994-10-14'
  , '1994-09-23'
  , 3
  , 84.81
  , 'LILA-Supermercado'
  , 'Carrera 52 con Ave. Bolívar #65-98 Llano Largo'
  , 'Barquisimeto'
  , 'Lara'
  , '3508'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10284
  , 'LEHMS'
  , 4
  , '1994-09-19'
  , '1994-10-17'
  , '1994-09-27'
  , 1
  , 76.56
  , 'Lehmanns Marktstand'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10285
  , 'QUICK'
  , 1
  , '1994-09-20'
  , '1994-10-18'
  , '1994-09-26'
  , 2
  , 76.83
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10286
  , 'QUICK'
  , 8
  , '1994-09-21'
  , '1994-10-19'
  , '1994-09-30'
  , 3
  , 229.24
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10287
  , 'RICAR'
  , 8
  , '1994-09-22'
  , '1994-10-20'
  , '1994-09-28'
  , 3
  , 12.76
  , 'Ricardo Adocicados'
  , 'Av. Copacabana, 267'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-890'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10288
  , 'REGGC'
  , 4
  , '1994-09-23'
  , '1994-10-21'
  , '1994-10-04'
  , 1
  , 7.45
  , 'Reggiani Caseifici'
  , 'Strada Provinciale 124'
  , 'Reggio Emilia'
  , NULL
  , '42100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10289
  , 'BSBEV'
  , 7
  , '1994-09-26'
  , '1994-10-24'
  , '1994-09-28'
  , 3
  , 22.77
  , 'B''s Beverages'
  , 'Fauntleroy Circus'
  , 'London'
  , NULL
  , 'EC2 5NT'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10290
  , 'COMMI'
  , 8
  , '1994-09-27'
  , '1994-10-25'
  , '1994-10-04'
  , 1
  , 79.70
  , 'Comércio Mineiro'
  , 'Av. dos Lusíadas, 23'
  , 'São Paulo'
  , 'SP'
  , '05432-043'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10291
  , 'QUEDE'
  , 6
  , '1994-09-27'
  , '1994-10-25'
  , '1994-10-05'
  , 2
  , 6.40
  , 'Que Delícia'
  , 'Rua da Panificadora, 12'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-673'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10292
  , 'TRADH'
  , 1
  , '1994-09-28'
  , '1994-10-26'
  , '1994-10-03'
  , 2
  , 1.35
  , 'Tradição Hipermercados'
  , 'Av. Inês de Castro, 414'
  , 'São Paulo'
  , 'SP'
  , '05634-030'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10293
  , 'TORTU'
  , 1
  , '1994-09-29'
  , '1994-10-27'
  , '1994-10-12'
  , 3
  , 21.18
  , 'Tortuga Restaurante'
  , 'Avda. Azteca 123'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10294
  , 'RATTC'
  , 4
  , '1994-09-30'
  , '1994-10-28'
  , '1994-10-06'
  , 2
  , 147.26
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10295
  , 'VINET'
  , 2
  , '1994-10-03'
  , '1994-10-31'
  , '1994-10-11'
  , 2
  , 1.15
  , 'Vins et alcools Chevalier'
  , '59 rue de l''Abbaye'
  , 'Reims'
  , NULL
  , '51100'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10296
  , 'LILAS'
  , 6
  , '1994-10-04'
  , '1994-11-01'
  , '1994-10-12'
  , 1
  , 0.12
  , 'LILA-Supermercado'
  , 'Carrera 52 con Ave. Bolívar #65-98 Llano Largo'
  , 'Barquisimeto'
  , 'Lara'
  , '3508'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10297
  , 'BLONP'
  , 5
  , '1994-10-05'
  , '1994-11-16'
  , '1994-10-11'
  , 2
  , 5.74
  , 'Blondel père et fils'
  , '24, place Kléber'
  , 'Strasbourg'
  , NULL
  , '67000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10298
  , 'HUNGO'
  , 6
  , '1994-10-06'
  , '1994-11-03'
  , '1994-10-12'
  , 2
  , 168.22
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10299
  , 'RICAR'
  , 4
  , '1994-10-07'
  , '1994-11-04'
  , '1994-10-14'
  , 2
  , 29.76
  , 'Ricardo Adocicados'
  , 'Av. Copacabana, 267'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-890'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10300
  , 'MAGAA'
  , 2
  , '1994-10-10'
  , '1994-11-07'
  , '1994-10-19'
  , 2
  , 17.68
  , 'Magazzini Alimentari Riuniti'
  , 'Via Ludovico il Moro 22'
  , 'Bergamo'
  , NULL
  , '24100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10301
  , 'WANDK'
  , 8
  , '1994-10-10'
  , '1994-11-07'
  , '1994-10-18'
  , 2
  , 45.08
  , 'Die Wandernde Kuh'
  , 'Adenauerallee 900'
  , 'Stuttgart'
  , NULL
  , '70563'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10302
  , 'SUPRD'
  , 4
  , '1994-10-11'
  , '1994-11-08'
  , '1994-11-09'
  , 2
  , 6.27
  , 'Suprêmes délices'
  , 'Boulevard Tirou, 255'
  , 'Charleroi'
  , NULL
  , 'B-6000'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10303
  , 'GODOS'
  , 7
  , '1994-10-12'
  , '1994-11-09'
  , '1994-10-19'
  , 2
  , 107.83
  , 'Godos Cocina Típica'
  , 'C/ Romero, 33'
  , 'Sevilla'
  , NULL
  , '41101'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10304
  , 'TORTU'
  , 1
  , '1994-10-13'
  , '1994-11-10'
  , '1994-10-18'
  , 2
  , 63.79
  , 'Tortuga Restaurante'
  , 'Avda. Azteca 123'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10305
  , 'OLDWO'
  , 8
  , '1994-10-14'
  , '1994-11-11'
  , '1994-11-09'
  , 3
  , 257.62
  , 'Old World Delicatessen'
  , '2743 Bering St.'
  , 'Anchorage'
  , 'AK'
  , '99508'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10306
  , 'ROMEY'
  , 1
  , '1994-10-17'
  , '1994-11-14'
  , '1994-10-24'
  , 3
  , 7.56
  , 'Romero y tomillo'
  , 'Gran Vía, 1'
  , 'Madrid'
  , NULL
  , '28001'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10307
  , 'LONEP'
  , 2
  , '1994-10-18'
  , '1994-11-15'
  , '1994-10-26'
  , 2
  , 0.56
  , 'Lonesome Pine Restaurant'
  , '89 Chiaroscuro Rd.'
  , 'Portland'
  , 'OR'
  , '97219'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10308
  , 'ANATR'
  , 7
  , '1994-10-19'
  , '1994-11-16'
  , '1994-10-25'
  , 3
  , 1.61
  , 'Ana Trujillo Emparedados y helados'
  , 'Avda. de la Constitución 2222'
  , 'México D.F.'
  , NULL
  , '05021'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10309
  , 'HUNGO'
  , 3
  , '1994-10-20'
  , '1994-11-17'
  , '1994-11-23'
  , 1
  , 47.30
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10310
  , 'THEBI'
  , 8
  , '1994-10-21'
  , '1994-11-18'
  , '1994-10-28'
  , 2
  , 17.52
  , 'The Big Cheese'
  , '89 Jefferson Way
Suite 2'
  , 'Portland'
  , 'OR'
  , '97201'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10311
  , 'DUMON'
  , 1
  , '1994-10-21'
  , '1994-11-04'
  , '1994-10-27'
  , 3
  , 24.69
  , 'Du monde entier'
  , '67, rue des Cinquante Otages'
  , 'Nantes'
  , NULL
  , '44000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10312
  , 'WANDK'
  , 2
  , '1994-10-24'
  , '1994-11-21'
  , '1994-11-03'
  , 2
  , 40.26
  , 'Die Wandernde Kuh'
  , 'Adenauerallee 900'
  , 'Stuttgart'
  , NULL
  , '70563'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10313
  , 'QUICK'
  , 2
  , '1994-10-25'
  , '1994-11-22'
  , '1994-11-04'
  , 2
  , 1.96
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10314
  , 'RATTC'
  , 1
  , '1994-10-26'
  , '1994-11-23'
  , '1994-11-04'
  , 2
  , 74.16
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10315
  , 'ISLAT'
  , 4
  , '1994-10-27'
  , '1994-11-24'
  , '1994-11-03'
  , 2
  , 41.76
  , 'Island Trading'
  , 'Garden House
Crowther Way'
  , 'Cowes'
  , 'Isle of Wight'
  , 'PO31 7PJ'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10316
  , 'RATTC'
  , 1
  , '1994-10-28'
  , '1994-11-25'
  , '1994-11-08'
  , 3
  , 150.15
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10317
  , 'LONEP'
  , 6
  , '1994-10-31'
  , '1994-11-28'
  , '1994-11-10'
  , 1
  , 12.69
  , 'Lonesome Pine Restaurant'
  , '89 Chiaroscuro Rd.'
  , 'Portland'
  , 'OR'
  , '97219'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10318
  , 'ISLAT'
  , 8
  , '1994-11-01'
  , '1994-11-29'
  , '1994-11-04'
  , 2
  , 4.73
  , 'Island Trading'
  , 'Garden House
Crowther Way'
  , 'Cowes'
  , 'Isle of Wight'
  , 'PO31 7PJ'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10319
  , 'TORTU'
  , 7
  , '1994-11-02'
  , '1994-11-30'
  , '1994-11-11'
  , 3
  , 64.50
  , 'Tortuga Restaurante'
  , 'Avda. Azteca 123'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10320
  , 'WARTH'
  , 5
  , '1994-11-03'
  , '1994-11-17'
  , '1994-11-18'
  , 3
  , 34.57
  , 'Wartian Herkku'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10321
  , 'ISLAT'
  , 3
  , '1994-11-03'
  , '1994-12-01'
  , '1994-11-11'
  , 2
  , 3.43
  , 'Island Trading'
  , 'Garden House
Crowther Way'
  , 'Cowes'
  , 'Isle of Wight'
  , 'PO31 7PJ'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10322
  , 'PERIC'
  , 7
  , '1994-11-04'
  , '1994-12-02'
  , '1994-11-23'
  , 3
  , 0.40
  , 'Pericles Comidas clásicas'
  , 'Calle Dr. Jorge Cash 321'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10323
  , 'KOENE'
  , 4
  , '1994-11-07'
  , '1994-12-05'
  , '1994-11-14'
  , 1
  , 4.88
  , 'Königlich Essen'
  , 'Maubelstr. 90'
  , 'Brandenburg'
  , NULL
  , '14776'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10324
  , 'SAVEA'
  , 9
  , '1994-11-08'
  , '1994-12-06'
  , '1994-11-10'
  , 1
  , 214.27
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10325
  , 'KOENE'
  , 1
  , '1994-11-09'
  , '1994-11-23'
  , '1994-11-14'
  , 3
  , 64.86
  , 'Königlich Essen'
  , 'Maubelstr. 90'
  , 'Brandenburg'
  , NULL
  , '14776'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10326
  , 'BOLID'
  , 4
  , '1994-11-10'
  , '1994-12-08'
  , '1994-11-14'
  , 2
  , 77.92
  , 'Bólido Comidas preparadas'
  , 'C/ Araquil, 67'
  , 'Madrid'
  , NULL
  , '28023'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10327
  , 'FOLKO'
  , 2
  , '1994-11-11'
  , '1994-12-09'
  , '1994-11-14'
  , 1
  , 63.36
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10328
  , 'FURIB'
  , 4
  , '1994-11-14'
  , '1994-12-12'
  , '1994-11-17'
  , 3
  , 87.03
  , 'Furia Bacalhau e Frutos do Mar'
  , 'Jardim das rosas n. 32'
  , 'Lisboa'
  , NULL
  , '1675'
  , 'Portugal'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10329
  , 'SPLIR'
  , 4
  , '1994-11-15'
  , '1994-12-27'
  , '1994-11-23'
  , 2
  , 191.67
  , 'Split Rail Beer & Ale'
  , 'P.O. Box 555'
  , 'Lander'
  , 'WY'
  , '82520'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10330
  , 'LILAS'
  , 3
  , '1994-11-16'
  , '1994-12-14'
  , '1994-11-28'
  , 1
  , 12.75
  , 'LILA-Supermercado'
  , 'Carrera 52 con Ave. Bolívar #65-98 Llano Largo'
  , 'Barquisimeto'
  , 'Lara'
  , '3508'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10331
  , 'BONAP'
  , 9
  , '1994-11-16'
  , '1994-12-28'
  , '1994-11-21'
  , 1
  , 10.19
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10332
  , 'MEREP'
  , 3
  , '1994-11-17'
  , '1994-12-29'
  , '1994-11-21'
  , 2
  , 52.84
  , 'Mère Paillarde'
  , '43 rue St. Laurent'
  , 'Montréal'
  , 'Québec'
  , 'H1J 1C3'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10333
  , 'WARTH'
  , 5
  , '1994-11-18'
  , '1994-12-16'
  , '1994-11-25'
  , 3
  , 0.59
  , 'Wartian Herkku'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10334
  , 'VICTE'
  , 8
  , '1994-11-21'
  , '1994-12-19'
  , '1994-11-28'
  , 2
  , 8.56
  , 'Victuailles en stock'
  , '2, rue du Commerce'
  , 'Lyon'
  , NULL
  , '69004'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10335
  , 'HUNGO'
  , 7
  , '1994-11-22'
  , '1994-12-20'
  , '1994-11-24'
  , 2
  , 42.11
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10336
  , 'PRINI'
  , 7
  , '1994-11-23'
  , '1994-12-21'
  , '1994-11-25'
  , 2
  , 15.51
  , 'Princesa Isabel Vinhos'
  , 'Estrada da saúde n. 58'
  , 'Lisboa'
  , NULL
  , '1756'
  , 'Portugal'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10337
  , 'FRANK'
  , 4
  , '1994-11-24'
  , '1994-12-22'
  , '1994-11-29'
  , 3
  , 108.26
  , 'Frankenversand'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10338
  , 'OLDWO'
  , 4
  , '1994-11-25'
  , '1994-12-23'
  , '1994-11-29'
  , 3
  , 84.21
  , 'Old World Delicatessen'
  , '2743 Bering St.'
  , 'Anchorage'
  , 'AK'
  , '99508'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10339
  , 'MEREP'
  , 2
  , '1994-11-28'
  , '1994-12-26'
  , '1994-12-05'
  , 2
  , 15.66
  , 'Mère Paillarde'
  , '43 rue St. Laurent'
  , 'Montréal'
  , 'Québec'
  , 'H1J 1C3'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10340
  , 'BONAP'
  , 1
  , '1994-11-29'
  , '1994-12-27'
  , '1994-12-09'
  , 3
  , 166.31
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10341
  , 'SIMOB'
  , 7
  , '1994-11-29'
  , '1994-12-27'
  , '1994-12-06'
  , 3
  , 26.78
  , 'Simons bistro'
  , 'Vinbæltet 34'
  , 'København'
  , NULL
  , '1734'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10342
  , 'FRANK'
  , 4
  , '1994-11-30'
  , '1994-12-14'
  , '1994-12-05'
  , 2
  , 54.83
  , 'Frankenversand'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10343
  , 'LEHMS'
  , 4
  , '1994-12-01'
  , '1994-12-29'
  , '1994-12-07'
  , 1
  , 110.37
  , 'Lehmanns Marktstand'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10344
  , 'WHITC'
  , 4
  , '1994-12-02'
  , '1994-12-30'
  , '1994-12-06'
  , 2
  , 23.29
  , 'White Clover Markets'
  , '1029 - 12th Ave. S.'
  , 'Seattle'
  , 'WA'
  , '98124'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10345
  , 'QUICK'
  , 2
  , '1994-12-05'
  , '1995-01-02'
  , '1994-12-12'
  , 2
  , 249.06
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10346
  , 'RATTC'
  , 3
  , '1994-12-06'
  , '1995-01-17'
  , '1994-12-09'
  , 3
  , 142.08
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10347
  , 'FAMIA'
  , 4
  , '1994-12-07'
  , '1995-01-04'
  , '1994-12-09'
  , 3
  , 3.10
  , 'Familia Arquibaldo'
  , 'Rua Orós, 92'
  , 'São Paulo'
  , 'SP'
  , '05442-030'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10348
  , 'WANDK'
  , 4
  , '1994-12-08'
  , '1995-01-05'
  , '1994-12-16'
  , 2
  , 0.78
  , 'Die Wandernde Kuh'
  , 'Adenauerallee 900'
  , 'Stuttgart'
  , NULL
  , '70563'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10349
  , 'SPLIR'
  , 7
  , '1994-12-09'
  , '1995-01-06'
  , '1994-12-16'
  , 1
  , 8.63
  , 'Split Rail Beer & Ale'
  , 'P.O. Box 555'
  , 'Lander'
  , 'WY'
  , '82520'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10350
  , 'LAMAI'
  , 6
  , '1994-12-12'
  , '1995-01-09'
  , '1995-01-03'
  , 2
  , 64.19
  , 'La maison d''Asie'
  , '1 rue Alsace-Lorraine'
  , 'Toulouse'
  , NULL
  , '31000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10351
  , 'ERNSH'
  , 1
  , '1994-12-12'
  , '1995-01-09'
  , '1994-12-21'
  , 1
  , 162.33
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10352
  , 'FURIB'
  , 3
  , '1994-12-13'
  , '1994-12-27'
  , '1994-12-19'
  , 3
  , 1.30
  , 'Furia Bacalhau e Frutos do Mar'
  , 'Jardim das rosas n. 32'
  , 'Lisboa'
  , NULL
  , '1675'
  , 'Portugal'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10353
  , 'PICCO'
  , 7
  , '1994-12-14'
  , '1995-01-11'
  , '1994-12-26'
  , 3
  , 360.63
  , 'Piccolo und mehr'
  , 'Geislweg 14'
  , 'Salzburg'
  , NULL
  , '5020'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10354
  , 'PERIC'
  , 8
  , '1994-12-15'
  , '1995-01-12'
  , '1994-12-21'
  , 3
  , 53.80
  , 'Pericles Comidas clásicas'
  , 'Calle Dr. Jorge Cash 321'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10355
  , 'AROUT'
  , 6
  , '1994-12-16'
  , '1995-01-13'
  , '1994-12-21'
  , 1
  , 41.95
  , 'Around the Horn'
  , 'Brook Farm
Stratford St. Mary'
  , 'Colchester'
  , 'Essex'
  , 'CO7 6JX'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10356
  , 'WANDK'
  , 6
  , '1994-12-19'
  , '1995-01-16'
  , '1994-12-28'
  , 2
  , 36.71
  , 'Die Wandernde Kuh'
  , 'Adenauerallee 900'
  , 'Stuttgart'
  , NULL
  , '70563'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10357
  , 'LILAS'
  , 1
  , '1994-12-20'
  , '1995-01-17'
  , '1995-01-02'
  , 3
  , 34.88
  , 'LILA-Supermercado'
  , 'Carrera 52 con Ave. Bolívar #65-98 Llano Largo'
  , 'Barquisimeto'
  , 'Lara'
  , '3508'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10358
  , 'LAMAI'
  , 5
  , '1994-12-21'
  , '1995-01-18'
  , '1994-12-28'
  , 1
  , 19.64
  , 'La maison d''Asie'
  , '1 rue Alsace-Lorraine'
  , 'Toulouse'
  , NULL
  , '31000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10359
  , 'SEVES'
  , 5
  , '1994-12-22'
  , '1995-01-19'
  , '1994-12-27'
  , 3
  , 288.43
  , 'Seven Seas Imports'
  , '90 Wadhurst Rd.'
  , 'London'
  , NULL
  , 'OX15 4NB'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10360
  , 'BLONP'
  , 4
  , '1994-12-23'
  , '1995-01-20'
  , '1995-01-02'
  , 3
  , 131.70
  , 'Blondel père et fils'
  , '24, place Kléber'
  , 'Strasbourg'
  , NULL
  , '67000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10361
  , 'QUICK'
  , 1
  , '1994-12-23'
  , '1995-01-20'
  , '1995-01-03'
  , 2
  , 183.17
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10362
  , 'BONAP'
  , 3
  , '1994-12-26'
  , '1995-01-23'
  , '1994-12-29'
  , 1
  , 96.04
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10363
  , 'DRACD'
  , 4
  , '1994-12-27'
  , '1995-01-24'
  , '1995-01-04'
  , 3
  , 30.54
  , 'Drachenblut Delikatessen'
  , 'Walserweg 21'
  , 'Aachen'
  , NULL
  , '52066'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10364
  , 'EASTC'
  , 1
  , '1994-12-27'
  , '1995-02-07'
  , '1995-01-04'
  , 1
  , 71.97
  , 'Eastern Connection'
  , '35 King George'
  , 'London'
  , NULL
  , 'WX3 6FW'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10365
  , 'ANTON'
  , 3
  , '1994-12-28'
  , '1995-01-25'
  , '1995-01-02'
  , 2
  , 22.00
  , 'Antonio Moreno Taquería'
  , 'Mataderos  2312'
  , 'México D.F.'
  , NULL
  , '05023'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10366
  , 'GALED'
  , 8
  , '1994-12-29'
  , '1995-02-09'
  , '1995-01-30'
  , 2
  , 10.14
  , 'Galería del gastronómo'
  , 'Rambla de Cataluña, 23'
  , 'Barcelona'
  , NULL
  , '8022'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10367
  , 'VAFFE'
  , 7
  , '1994-12-29'
  , '1995-01-26'
  , '1995-01-02'
  , 3
  , 13.55
  , 'Vaffeljernet'
  , 'Smagsløget 45'
  , 'Århus'
  , NULL
  , '8200'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10368
  , 'ERNSH'
  , 2
  , '1994-12-30'
  , '1995-01-27'
  , '1995-01-02'
  , 2
  , 101.95
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10369
  , 'SPLIR'
  , 8
  , '1995-01-02'
  , '1995-01-30'
  , '1995-01-09'
  , 2
  , 195.68
  , 'Split Rail Beer & Ale'
  , 'P.O. Box 555'
  , 'Lander'
  , 'WY'
  , '82520'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10370
  , 'CHOPS'
  , 6
  , '1995-01-03'
  , '1995-01-31'
  , '1995-01-27'
  , 2
  , 1.17
  , 'Chop-suey Chinese'
  , 'Hauptstr. 31'
  , 'Bern'
  , NULL
  , '3012'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10371
  , 'LAMAI'
  , 1
  , '1995-01-03'
  , '1995-01-31'
  , '1995-01-24'
  , 1
  , 0.45
  , 'La maison d''Asie'
  , '1 rue Alsace-Lorraine'
  , 'Toulouse'
  , NULL
  , '31000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10372
  , 'QUEEN'
  , 5
  , '1995-01-04'
  , '1995-02-01'
  , '1995-01-09'
  , 2
  , 890.78
  , 'Queen Cozinha'
  , 'Alameda dos Canàrios, 891'
  , 'São Paulo'
  , 'SP'
  , '05487-020'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10373
  , 'HUNGO'
  , 4
  , '1995-01-05'
  , '1995-02-02'
  , '1995-01-11'
  , 3
  , 124.12
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10374
  , 'WOLZA'
  , 1
  , '1995-01-05'
  , '1995-02-02'
  , '1995-01-09'
  , 3
  , 3.94
  , 'Wolski Zajazd'
  , 'ul. Filtrowa 68'
  , 'Warszawa'
  , NULL
  , '01-012'
  , 'Poland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10375
  , 'HUNGC'
  , 3
  , '1995-01-06'
  , '1995-02-03'
  , '1995-01-09'
  , 2
  , 20.12
  , 'Hungry Coyote Import Store'
  , 'City Center Plaza
516 Main St.'
  , 'Elgin'
  , 'OR'
  , '97827'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10376
  , 'MEREP'
  , 1
  , '1995-01-09'
  , '1995-02-06'
  , '1995-01-13'
  , 2
  , 20.39
  , 'Mère Paillarde'
  , '43 rue St. Laurent'
  , 'Montréal'
  , 'Québec'
  , 'H1J 1C3'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10377
  , 'SEVES'
  , 1
  , '1995-01-09'
  , '1995-02-06'
  , '1995-01-13'
  , 3
  , 22.21
  , 'Seven Seas Imports'
  , '90 Wadhurst Rd.'
  , 'London'
  , NULL
  , 'OX15 4NB'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10378
  , 'FOLKO'
  , 5
  , '1995-01-10'
  , '1995-02-07'
  , '1995-01-19'
  , 3
  , 5.44
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10379
  , 'QUEDE'
  , 2
  , '1995-01-11'
  , '1995-02-08'
  , '1995-01-13'
  , 1
  , 45.03
  , 'Que Delícia'
  , 'Rua da Panificadora, 12'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-673'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10380
  , 'HUNGO'
  , 8
  , '1995-01-12'
  , '1995-02-09'
  , '1995-02-16'
  , 3
  , 35.03
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10381
  , 'LILAS'
  , 3
  , '1995-01-12'
  , '1995-02-09'
  , '1995-01-13'
  , 3
  , 7.99
  , 'LILA-Supermercado'
  , 'Carrera 52 con Ave. Bolívar #65-98 Llano Largo'
  , 'Barquisimeto'
  , 'Lara'
  , '3508'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10382
  , 'ERNSH'
  , 4
  , '1995-01-13'
  , '1995-02-10'
  , '1995-01-16'
  , 1
  , 94.77
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10383
  , 'AROUT'
  , 8
  , '1995-01-16'
  , '1995-02-13'
  , '1995-01-18'
  , 3
  , 34.24
  , 'Around the Horn'
  , 'Brook Farm
Stratford St. Mary'
  , 'Colchester'
  , 'Essex'
  , 'CO7 6JX'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10384
  , 'BERGS'
  , 3
  , '1995-01-16'
  , '1995-02-13'
  , '1995-01-20'
  , 3
  , 168.64
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10385
  , 'SPLIR'
  , 1
  , '1995-01-17'
  , '1995-02-14'
  , '1995-01-23'
  , 2
  , 30.96
  , 'Split Rail Beer & Ale'
  , 'P.O. Box 555'
  , 'Lander'
  , 'WY'
  , '82520'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10386
  , 'FAMIA'
  , 9
  , '1995-01-18'
  , '1995-02-01'
  , '1995-01-25'
  , 3
  , 13.99
  , 'Familia Arquibaldo'
  , 'Rua Orós, 92'
  , 'São Paulo'
  , 'SP'
  , '05442-030'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10387
  , 'SANTG'
  , 1
  , '1995-01-18'
  , '1995-02-15'
  , '1995-01-20'
  , 2
  , 93.63
  , 'Sant?Gourmet'
  , 'Erling Skakkes gate 78'
  , 'Stavern'
  , NULL
  , '4110'
  , 'Norway'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10388
  , 'SEVES'
  , 2
  , '1995-01-19'
  , '1995-02-16'
  , '1995-01-20'
  , 1
  , 34.86
  , 'Seven Seas Imports'
  , '90 Wadhurst Rd.'
  , 'London'
  , NULL
  , 'OX15 4NB'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10389
  , 'BOTTM'
  , 4
  , '1995-01-20'
  , '1995-02-17'
  , '1995-01-24'
  , 2
  , 47.42
  , 'Bottom-Dollar Markets'
  , '23 Tsawassen Blvd.'
  , 'Tsawassen'
  , 'BC'
  , 'T2F 8M4'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10390
  , 'ERNSH'
  , 6
  , '1995-01-23'
  , '1995-02-20'
  , '1995-01-26'
  , 1
  , 126.38
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10391
  , 'DRACD'
  , 3
  , '1995-01-23'
  , '1995-02-20'
  , '1995-01-31'
  , 3
  , 5.45
  , 'Drachenblut Delikatessen'
  , 'Walserweg 21'
  , 'Aachen'
  , NULL
  , '52066'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10392
  , 'PICCO'
  , 2
  , '1995-01-24'
  , '1995-02-21'
  , '1995-02-01'
  , 3
  , 122.46
  , 'Piccolo und mehr'
  , 'Geislweg 14'
  , 'Salzburg'
  , NULL
  , '5020'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10393
  , 'SAVEA'
  , 1
  , '1995-01-25'
  , '1995-02-22'
  , '1995-02-03'
  , 3
  , 126.56
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10394
  , 'HUNGC'
  , 1
  , '1995-01-25'
  , '1995-02-22'
  , '1995-02-03'
  , 3
  , 30.34
  , 'Hungry Coyote Import Store'
  , 'City Center Plaza
516 Main St.'
  , 'Elgin'
  , 'OR'
  , '97827'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10395
  , 'HILAA'
  , 6
  , '1995-01-26'
  , '1995-02-23'
  , '1995-02-03'
  , 1
  , 184.41
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10396
  , 'FRANK'
  , 1
  , '1995-01-27'
  , '1995-02-10'
  , '1995-02-06'
  , 3
  , 135.35
  , 'Frankenversand'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10397
  , 'PRINI'
  , 5
  , '1995-01-27'
  , '1995-02-24'
  , '1995-02-02'
  , 1
  , 60.26
  , 'Princesa Isabel Vinhos'
  , 'Estrada da saúde n. 58'
  , 'Lisboa'
  , NULL
  , '1756'
  , 'Portugal'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10398
  , 'SAVEA'
  , 2
  , '1995-01-30'
  , '1995-02-27'
  , '1995-02-09'
  , 3
  , 89.16
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10399
  , 'VAFFE'
  , 8
  , '1995-01-31'
  , '1995-02-14'
  , '1995-02-08'
  , 3
  , 27.36
  , 'Vaffeljernet'
  , 'Smagsløget 45'
  , 'Århus'
  , NULL
  , '8200'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10400
  , 'EASTC'
  , 1
  , '1995-02-01'
  , '1995-03-01'
  , '1995-02-16'
  , 3
  , 83.93
  , 'Eastern Connection'
  , '35 King George'
  , 'London'
  , NULL
  , 'WX3 6FW'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10401
  , 'RATTC'
  , 1
  , '1995-02-01'
  , '1995-03-01'
  , '1995-02-10'
  , 1
  , 12.51
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10402
  , 'ERNSH'
  , 8
  , '1995-02-02'
  , '1995-03-16'
  , '1995-02-10'
  , 2
  , 67.88
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10403
  , 'ERNSH'
  , 4
  , '1995-02-03'
  , '1995-03-03'
  , '1995-02-09'
  , 3
  , 73.79
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10404
  , 'MAGAA'
  , 2
  , '1995-02-03'
  , '1995-03-03'
  , '1995-02-08'
  , 1
  , 155.97
  , 'Magazzini Alimentari Riuniti'
  , 'Via Ludovico il Moro 22'
  , 'Bergamo'
  , NULL
  , '24100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10405
  , 'LINOD'
  , 1
  , '1995-02-06'
  , '1995-03-06'
  , '1995-02-22'
  , 1
  , 34.82
  , 'LINO-Delicateses'
  , 'Ave. 5 de Mayo Porlamar'
  , 'I. de Margarita'
  , 'Nueva Esparta'
  , '4980'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10406
  , 'QUEEN'
  , 7
  , '1995-02-07'
  , '1995-03-21'
  , '1995-02-13'
  , 1
  , 108.04
  , 'Queen Cozinha'
  , 'Alameda dos Canàrios, 891'
  , 'São Paulo'
  , 'SP'
  , '05487-020'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10407
  , 'OTTIK'
  , 2
  , '1995-02-07'
  , '1995-03-07'
  , '1995-03-02'
  , 2
  , 91.48
  , 'Ottilies Käseladen'
  , 'Mehrheimerstr. 369'
  , 'Köln'
  , NULL
  , '50739'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10408
  , 'FOLIG'
  , 8
  , '1995-02-08'
  , '1995-03-08'
  , '1995-02-14'
  , 1
  , 11.26
  , 'Folies gourmandes'
  , '184, chaussée de Tournai'
  , 'Lille'
  , NULL
  , '59000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10409
  , 'OCEAN'
  , 3
  , '1995-02-09'
  , '1995-03-09'
  , '1995-02-14'
  , 1
  , 29.83
  , 'Océano Atlántico Ltda.'
  , 'Ing. Gustavo Moncada 8585
Piso 20-A'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10410
  , 'BOTTM'
  , 3
  , '1995-02-10'
  , '1995-03-10'
  , '1995-02-15'
  , 3
  , 2.40
  , 'Bottom-Dollar Markets'
  , '23 Tsawassen Blvd.'
  , 'Tsawassen'
  , 'BC'
  , 'T2F 8M4'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10411
  , 'BOTTM'
  , 9
  , '1995-02-10'
  , '1995-03-10'
  , '1995-02-21'
  , 3
  , 23.65
  , 'Bottom-Dollar Markets'
  , '23 Tsawassen Blvd.'
  , 'Tsawassen'
  , 'BC'
  , 'T2F 8M4'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10412
  , 'WARTH'
  , 8
  , '1995-02-13'
  , '1995-03-13'
  , '1995-02-15'
  , 2
  , 3.77
  , 'Wartian Herkku'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10413
  , 'LAMAI'
  , 3
  , '1995-02-14'
  , '1995-03-14'
  , '1995-02-16'
  , 2
  , 95.66
  , 'La maison d''Asie'
  , '1 rue Alsace-Lorraine'
  , 'Toulouse'
  , NULL
  , '31000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10414
  , 'FAMIA'
  , 2
  , '1995-02-14'
  , '1995-03-14'
  , '1995-02-17'
  , 3
  , 21.48
  , 'Familia Arquibaldo'
  , 'Rua Orós, 92'
  , 'São Paulo'
  , 'SP'
  , '05442-030'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10415
  , 'HUNGC'
  , 3
  , '1995-02-15'
  , '1995-03-15'
  , '1995-02-24'
  , 1
  , 0.20
  , 'Hungry Coyote Import Store'
  , 'City Center Plaza
516 Main St.'
  , 'Elgin'
  , 'OR'
  , '97827'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10416
  , 'WARTH'
  , 8
  , '1995-02-16'
  , '1995-03-16'
  , '1995-02-27'
  , 3
  , 22.72
  , 'Wartian Herkku'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10417
  , 'SIMOB'
  , 4
  , '1995-02-16'
  , '1995-03-16'
  , '1995-02-28'
  , 3
  , 70.29
  , 'Simons bistro'
  , 'Vinbæltet 34'
  , 'København'
  , NULL
  , '1734'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10418
  , 'QUICK'
  , 4
  , '1995-02-17'
  , '1995-03-17'
  , '1995-02-24'
  , 1
  , 17.55
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10419
  , 'RICSU'
  , 4
  , '1995-02-20'
  , '1995-03-20'
  , '1995-03-02'
  , 2
  , 137.35
  , 'Richter Supermarkt'
  , 'Starenweg 5'
  , 'Genève'
  , NULL
  , '1204'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10420
  , 'WELLI'
  , 3
  , '1995-02-21'
  , '1995-03-21'
  , '1995-02-27'
  , 1
  , 44.12
  , 'Wellington Importadora'
  , 'Rua do Mercado, 12'
  , 'Resende'
  , 'SP'
  , '08737-363'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10421
  , 'QUEDE'
  , 8
  , '1995-02-21'
  , '1995-04-04'
  , '1995-02-27'
  , 1
  , 99.23
  , 'Que Delícia'
  , 'Rua da Panificadora, 12'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-673'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10422
  , 'FRANS'
  , 2
  , '1995-02-22'
  , '1995-03-22'
  , '1995-03-03'
  , 1
  , 3.02
  , 'Franchi S.p.A.'
  , 'Via Monte Bianco 34'
  , 'Torino'
  , NULL
  , '10100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10423
  , 'GOURL'
  , 6
  , '1995-02-23'
  , '1995-03-09'
  , '1995-03-27'
  , 3
  , 24.50
  , 'Gourmet Lanchonetes'
  , 'Av. Brasil, 442'
  , 'Campinas'
  , 'SP'
  , '04876-786'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10424
  , 'MEREP'
  , 7
  , '1995-02-23'
  , '1995-03-23'
  , '1995-02-27'
  , 2
  , 370.61
  , 'Mère Paillarde'
  , '43 rue St. Laurent'
  , 'Montréal'
  , 'Québec'
  , 'H1J 1C3'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10425
  , 'LAMAI'
  , 6
  , '1995-02-24'
  , '1995-03-24'
  , '1995-03-17'
  , 2
  , 7.93
  , 'La maison d''Asie'
  , '1 rue Alsace-Lorraine'
  , 'Toulouse'
  , NULL
  , '31000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10426
  , 'GALED'
  , 4
  , '1995-02-27'
  , '1995-03-27'
  , '1995-03-09'
  , 1
  , 18.69
  , 'Galería del gastronómo'
  , 'Rambla de Cataluña, 23'
  , 'Barcelona'
  , NULL
  , '8022'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10427
  , 'PICCO'
  , 4
  , '1995-02-27'
  , '1995-03-27'
  , '1995-04-03'
  , 2
  , 31.29
  , 'Piccolo und mehr'
  , 'Geislweg 14'
  , 'Salzburg'
  , NULL
  , '5020'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10428
  , 'REGGC'
  , 7
  , '1995-02-28'
  , '1995-03-28'
  , '1995-03-07'
  , 1
  , 11.09
  , 'Reggiani Caseifici'
  , 'Strada Provinciale 124'
  , 'Reggio Emilia'
  , NULL
  , '42100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10429
  , 'HUNGO'
  , 3
  , '1995-03-01'
  , '1995-04-12'
  , '1995-03-10'
  , 2
  , 56.63
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10430
  , 'ERNSH'
  , 4
  , '1995-03-02'
  , '1995-03-16'
  , '1995-03-06'
  , 1
  , 458.78
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10431
  , 'BOTTM'
  , 4
  , '1995-03-02'
  , '1995-03-16'
  , '1995-03-10'
  , 2
  , 44.17
  , 'Bottom-Dollar Markets'
  , '23 Tsawassen Blvd.'
  , 'Tsawassen'
  , 'BC'
  , 'T2F 8M4'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10432
  , 'SPLIR'
  , 3
  , '1995-03-03'
  , '1995-03-17'
  , '1995-03-10'
  , 2
  , 4.34
  , 'Split Rail Beer & Ale'
  , 'P.O. Box 555'
  , 'Lander'
  , 'WY'
  , '82520'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10433
  , 'PRINI'
  , 3
  , '1995-03-06'
  , '1995-04-03'
  , '1995-04-04'
  , 3
  , 73.83
  , 'Princesa Isabel Vinhos'
  , 'Estrada da saúde n. 58'
  , 'Lisboa'
  , NULL
  , '1756'
  , 'Portugal'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10434
  , 'FOLKO'
  , 3
  , '1995-03-06'
  , '1995-04-03'
  , '1995-03-16'
  , 2
  , 17.92
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10435
  , 'CONSH'
  , 8
  , '1995-03-07'
  , '1995-04-18'
  , '1995-03-10'
  , 2
  , 9.21
  , 'Consolidated Holdings'
  , 'Berkeley Gardens
12  Brewery '
  , 'London'
  , NULL
  , 'WX1 6LT'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10436
  , 'BLONP'
  , 3
  , '1995-03-08'
  , '1995-04-05'
  , '1995-03-14'
  , 2
  , 156.66
  , 'Blondel père et fils'
  , '24, place Kléber'
  , 'Strasbourg'
  , NULL
  , '67000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10437
  , 'WARTH'
  , 8
  , '1995-03-08'
  , '1995-04-05'
  , '1995-03-15'
  , 1
  , 19.97
  , 'Wartian Herkku'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10438
  , 'TOMSP'
  , 3
  , '1995-03-09'
  , '1995-04-06'
  , '1995-03-17'
  , 2
  , 8.24
  , 'Toms Spezialitäten'
  , 'Luisenstr. 48'
  , 'Münster'
  , NULL
  , '44087'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10439
  , 'MEREP'
  , 6
  , '1995-03-10'
  , '1995-04-07'
  , '1995-03-13'
  , 3
  , 4.07
  , 'Mère Paillarde'
  , '43 rue St. Laurent'
  , 'Montréal'
  , 'Québec'
  , 'H1J 1C3'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10440
  , 'SAVEA'
  , 4
  , '1995-03-13'
  , '1995-04-10'
  , '1995-03-31'
  , 2
  , 86.53
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10441
  , 'OLDWO'
  , 3
  , '1995-03-13'
  , '1995-04-24'
  , '1995-04-14'
  , 2
  , 73.02
  , 'Old World Delicatessen'
  , '2743 Bering St.'
  , 'Anchorage'
  , 'AK'
  , '99508'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10442
  , 'ERNSH'
  , 3
  , '1995-03-14'
  , '1995-04-11'
  , '1995-03-21'
  , 2
  , 47.94
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10443
  , 'REGGC'
  , 8
  , '1995-03-15'
  , '1995-04-12'
  , '1995-03-17'
  , 1
  , 13.95
  , 'Reggiani Caseifici'
  , 'Strada Provinciale 124'
  , 'Reggio Emilia'
  , NULL
  , '42100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10444
  , 'BERGS'
  , 3
  , '1995-03-15'
  , '1995-04-12'
  , '1995-03-24'
  , 3
  , 3.50
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10445
  , 'BERGS'
  , 3
  , '1995-03-16'
  , '1995-04-13'
  , '1995-03-23'
  , 1
  , 9.30
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10446
  , 'TOMSP'
  , 6
  , '1995-03-17'
  , '1995-04-14'
  , '1995-03-22'
  , 1
  , 14.68
  , 'Toms Spezialitäten'
  , 'Luisenstr. 48'
  , 'Münster'
  , NULL
  , '44087'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10447
  , 'RICAR'
  , 4
  , '1995-03-17'
  , '1995-04-14'
  , '1995-04-07'
  , 2
  , 68.66
  , 'Ricardo Adocicados'
  , 'Av. Copacabana, 267'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-890'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10448
  , 'RANCH'
  , 4
  , '1995-03-20'
  , '1995-04-17'
  , '1995-03-27'
  , 2
  , 38.82
  , 'Rancho grande'
  , 'Av. del Libertador 900'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10449
  , 'BLONP'
  , 3
  , '1995-03-21'
  , '1995-04-18'
  , '1995-03-30'
  , 2
  , 53.30
  , 'Blondel père et fils'
  , '24, place Kléber'
  , 'Strasbourg'
  , NULL
  , '67000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10450
  , 'VICTE'
  , 8
  , '1995-03-22'
  , '1995-04-19'
  , '1995-04-11'
  , 2
  , 7.23
  , 'Victuailles en stock'
  , '2, rue du Commerce'
  , 'Lyon'
  , NULL
  , '69004'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10451
  , 'QUICK'
  , 4
  , '1995-03-22'
  , '1995-04-05'
  , '1995-04-12'
  , 3
  , 189.09
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10452
  , 'SAVEA'
  , 8
  , '1995-03-23'
  , '1995-04-20'
  , '1995-03-29'
  , 1
  , 140.26
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10453
  , 'AROUT'
  , 1
  , '1995-03-24'
  , '1995-04-21'
  , '1995-03-29'
  , 2
  , 25.36
  , 'Around the Horn'
  , 'Brook Farm
Stratford St. Mary'
  , 'Colchester'
  , 'Essex'
  , 'CO7 6JX'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10454
  , 'LAMAI'
  , 4
  , '1995-03-24'
  , '1995-04-21'
  , '1995-03-28'
  , 3
  , 2.74
  , 'La maison d''Asie'
  , '1 rue Alsace-Lorraine'
  , 'Toulouse'
  , NULL
  , '31000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10455
  , 'WARTH'
  , 8
  , '1995-03-27'
  , '1995-05-08'
  , '1995-04-03'
  , 2
  , 180.45
  , 'Wartian Herkku'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10456
  , 'KOENE'
  , 8
  , '1995-03-28'
  , '1995-05-09'
  , '1995-03-31'
  , 2
  , 8.12
  , 'Königlich Essen'
  , 'Maubelstr. 90'
  , 'Brandenburg'
  , NULL
  , '14776'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10457
  , 'KOENE'
  , 2
  , '1995-03-28'
  , '1995-04-25'
  , '1995-04-03'
  , 1
  , 11.57
  , 'Königlich Essen'
  , 'Maubelstr. 90'
  , 'Brandenburg'
  , NULL
  , '14776'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10458
  , 'SUPRD'
  , 7
  , '1995-03-29'
  , '1995-04-26'
  , '1995-04-04'
  , 3
  , 147.06
  , 'Suprêmes délices'
  , 'Boulevard Tirou, 255'
  , 'Charleroi'
  , NULL
  , 'B-6000'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10459
  , 'VICTE'
  , 4
  , '1995-03-30'
  , '1995-04-27'
  , '1995-03-31'
  , 2
  , 25.09
  , 'Victuailles en stock'
  , '2, rue du Commerce'
  , 'Lyon'
  , NULL
  , '69004'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10460
  , 'FOLKO'
  , 8
  , '1995-03-31'
  , '1995-04-28'
  , '1995-04-03'
  , 1
  , 16.27
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10461
  , 'LILAS'
  , 1
  , '1995-03-31'
  , '1995-04-28'
  , '1995-04-05'
  , 3
  , 148.61
  , 'LILA-Supermercado'
  , 'Carrera 52 con Ave. Bolívar #65-98 Llano Largo'
  , 'Barquisimeto'
  , 'Lara'
  , '3508'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10462
  , 'CONSH'
  , 2
  , '1995-04-03'
  , '1995-05-01'
  , '1995-04-18'
  , 1
  , 6.17
  , 'Consolidated Holdings'
  , 'Berkeley Gardens
12  Brewery '
  , 'London'
  , NULL
  , 'WX1 6LT'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10463
  , 'SUPRD'
  , 5
  , '1995-04-04'
  , '1995-05-02'
  , '1995-04-06'
  , 3
  , 14.78
  , 'Suprêmes délices'
  , 'Boulevard Tirou, 255'
  , 'Charleroi'
  , NULL
  , 'B-6000'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10464
  , 'FURIB'
  , 4
  , '1995-04-04'
  , '1995-05-02'
  , '1995-04-14'
  , 2
  , 89.00
  , 'Furia Bacalhau e Frutos do Mar'
  , 'Jardim das rosas n. 32'
  , 'Lisboa'
  , NULL
  , '1675'
  , 'Portugal'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10465
  , 'VAFFE'
  , 1
  , '1995-04-05'
  , '1995-05-03'
  , '1995-04-14'
  , 3
  , 145.04
  , 'Vaffeljernet'
  , 'Smagsløget 45'
  , 'Århus'
  , NULL
  , '8200'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10466
  , 'COMMI'
  , 4
  , '1995-04-06'
  , '1995-05-04'
  , '1995-04-13'
  , 1
  , 11.93
  , 'Comércio Mineiro'
  , 'Av. dos Lusíadas, 23'
  , 'São Paulo'
  , 'SP'
  , '05432-043'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10467
  , 'MAGAA'
  , 8
  , '1995-04-06'
  , '1995-05-04'
  , '1995-04-11'
  , 2
  , 4.93
  , 'Magazzini Alimentari Riuniti'
  , 'Via Ludovico il Moro 22'
  , 'Bergamo'
  , NULL
  , '24100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10468
  , 'KOENE'
  , 3
  , '1995-04-07'
  , '1995-05-05'
  , '1995-04-12'
  , 3
  , 44.12
  , 'Königlich Essen'
  , 'Maubelstr. 90'
  , 'Brandenburg'
  , NULL
  , '14776'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10469
  , 'WHITC'
  , 1
  , '1995-04-10'
  , '1995-05-08'
  , '1995-04-14'
  , 1
  , 60.18
  , 'White Clover Markets'
  , '1029 - 12th Ave. S.'
  , 'Seattle'
  , 'WA'
  , '98124'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10470
  , 'BONAP'
  , 4
  , '1995-04-11'
  , '1995-05-09'
  , '1995-04-14'
  , 2
  , 64.56
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10471
  , 'BSBEV'
  , 2
  , '1995-04-11'
  , '1995-05-09'
  , '1995-04-18'
  , 3
  , 45.59
  , 'B''s Beverages'
  , 'Fauntleroy Circus'
  , 'London'
  , NULL
  , 'EC2 5NT'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10472
  , 'SEVES'
  , 8
  , '1995-04-12'
  , '1995-05-10'
  , '1995-04-19'
  , 1
  , 4.20
  , 'Seven Seas Imports'
  , '90 Wadhurst Rd.'
  , 'London'
  , NULL
  , 'OX15 4NB'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10473
  , 'ISLAT'
  , 1
  , '1995-04-13'
  , '1995-04-27'
  , '1995-04-21'
  , 3
  , 16.37
  , 'Island Trading'
  , 'Garden House
Crowther Way'
  , 'Cowes'
  , 'Isle of Wight'
  , 'PO31 7PJ'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10474
  , 'PERIC'
  , 5
  , '1995-04-13'
  , '1995-05-11'
  , '1995-04-21'
  , 2
  , 83.49
  , 'Pericles Comidas clásicas'
  , 'Calle Dr. Jorge Cash 321'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10475
  , 'SUPRD'
  , 9
  , '1995-04-14'
  , '1995-05-12'
  , '1995-05-05'
  , 1
  , 68.52
  , 'Suprêmes délices'
  , 'Boulevard Tirou, 255'
  , 'Charleroi'
  , NULL
  , 'B-6000'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10476
  , 'HILAA'
  , 8
  , '1995-04-17'
  , '1995-05-15'
  , '1995-04-24'
  , 3
  , 4.41
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10477
  , 'PRINI'
  , 5
  , '1995-04-17'
  , '1995-05-15'
  , '1995-04-25'
  , 2
  , 13.02
  , 'Princesa Isabel Vinhos'
  , 'Estrada da saúde n. 58'
  , 'Lisboa'
  , NULL
  , '1756'
  , 'Portugal'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10478
  , 'VICTE'
  , 2
  , '1995-04-18'
  , '1995-05-02'
  , '1995-04-26'
  , 3
  , 4.81
  , 'Victuailles en stock'
  , '2, rue du Commerce'
  , 'Lyon'
  , NULL
  , '69004'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10479
  , 'RATTC'
  , 3
  , '1995-04-19'
  , '1995-05-17'
  , '1995-04-21'
  , 3
  , 708.95
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10480
  , 'FOLIG'
  , 6
  , '1995-04-20'
  , '1995-05-18'
  , '1995-04-24'
  , 2
  , 1.35
  , 'Folies gourmandes'
  , '184, chaussée de Tournai'
  , 'Lille'
  , NULL
  , '59000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10481
  , 'RICAR'
  , 8
  , '1995-04-20'
  , '1995-05-18'
  , '1995-04-25'
  , 2
  , 64.33
  , 'Ricardo Adocicados'
  , 'Av. Copacabana, 267'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-890'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10482
  , 'LAZYK'
  , 1
  , '1995-04-21'
  , '1995-05-19'
  , '1995-05-11'
  , 3
  , 7.48
  , 'Lazy K Kountry Store'
  , '12 Orchestra Terrace'
  , 'Walla Walla'
  , 'WA'
  , '99362'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10483
  , 'WHITC'
  , 7
  , '1995-04-24'
  , '1995-05-22'
  , '1995-05-26'
  , 2
  , 15.28
  , 'White Clover Markets'
  , '1029 - 12th Ave. S.'
  , 'Seattle'
  , 'WA'
  , '98124'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10484
  , 'BSBEV'
  , 3
  , '1995-04-24'
  , '1995-05-22'
  , '1995-05-02'
  , 3
  , 6.88
  , 'B''s Beverages'
  , 'Fauntleroy Circus'
  , 'London'
  , NULL
  , 'EC2 5NT'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10485
  , 'LINOD'
  , 4
  , '1995-04-25'
  , '1995-05-09'
  , '1995-05-01'
  , 2
  , 64.45
  , 'LINO-Delicateses'
  , 'Ave. 5 de Mayo Porlamar'
  , 'I. de Margarita'
  , 'Nueva Esparta'
  , '4980'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10486
  , 'HILAA'
  , 1
  , '1995-04-26'
  , '1995-05-24'
  , '1995-05-03'
  , 2
  , 30.53
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10487
  , 'QUEEN'
  , 2
  , '1995-04-26'
  , '1995-05-24'
  , '1995-04-28'
  , 2
  , 71.07
  , 'Queen Cozinha'
  , 'Alameda dos Canàrios, 891'
  , 'São Paulo'
  , 'SP'
  , '05487-020'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10488
  , 'FRANK'
  , 8
  , '1995-04-27'
  , '1995-05-25'
  , '1995-05-03'
  , 2
  , 4.93
  , 'Frankenversand'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10489
  , 'PICCO'
  , 6
  , '1995-04-28'
  , '1995-05-26'
  , '1995-05-10'
  , 2
  , 5.29
  , 'Piccolo und mehr'
  , 'Geislweg 14'
  , 'Salzburg'
  , NULL
  , '5020'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10490
  , 'HILAA'
  , 7
  , '1995-05-01'
  , '1995-05-29'
  , '1995-05-04'
  , 2
  , 210.19
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10491
  , 'FURIB'
  , 8
  , '1995-05-01'
  , '1995-05-29'
  , '1995-05-09'
  , 3
  , 16.96
  , 'Furia Bacalhau e Frutos do Mar'
  , 'Jardim das rosas n. 32'
  , 'Lisboa'
  , NULL
  , '1675'
  , 'Portugal'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10492
  , 'BOTTM'
  , 3
  , '1995-05-02'
  , '1995-05-30'
  , '1995-05-12'
  , 1
  , 62.89
  , 'Bottom-Dollar Markets'
  , '23 Tsawassen Blvd.'
  , 'Tsawassen'
  , 'BC'
  , 'T2F 8M4'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10493
  , 'LAMAI'
  , 4
  , '1995-05-03'
  , '1995-05-31'
  , '1995-05-11'
  , 3
  , 10.64
  , 'La maison d''Asie'
  , '1 rue Alsace-Lorraine'
  , 'Toulouse'
  , NULL
  , '31000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10494
  , 'COMMI'
  , 4
  , '1995-05-03'
  , '1995-05-31'
  , '1995-05-10'
  , 2
  , 65.99
  , 'Comércio Mineiro'
  , 'Av. dos Lusíadas, 23'
  , 'São Paulo'
  , 'SP'
  , '05432-043'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10495
  , 'LAUGB'
  , 3
  , '1995-05-04'
  , '1995-06-01'
  , '1995-05-12'
  , 3
  , 4.65
  , 'Laughing Bacchus Wine Cellars'
  , '2319 Elm St.'
  , 'Vancouver'
  , 'BC'
  , 'V3F 2K1'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10496
  , 'TRADH'
  , 7
  , '1995-05-05'
  , '1995-06-02'
  , '1995-05-08'
  , 2
  , 46.77
  , 'Tradição Hipermercados'
  , 'Av. Inês de Castro, 414'
  , 'São Paulo'
  , 'SP'
  , '05634-030'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10497
  , 'LEHMS'
  , 7
  , '1995-05-05'
  , '1995-06-02'
  , '1995-05-08'
  , 1
  , 36.21
  , 'Lehmanns Marktstand'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10498
  , 'HILAA'
  , 8
  , '1995-05-08'
  , '1995-06-05'
  , '1995-05-12'
  , 2
  , 29.75
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10499
  , 'LILAS'
  , 4
  , '1995-05-09'
  , '1995-06-06'
  , '1995-05-17'
  , 2
  , 102.02
  , 'LILA-Supermercado'
  , 'Carrera 52 con Ave. Bolívar #65-98 Llano Largo'
  , 'Barquisimeto'
  , 'Lara'
  , '3508'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10500
  , 'LAMAI'
  , 6
  , '1995-05-10'
  , '1995-06-07'
  , '1995-05-18'
  , 1
  , 42.68
  , 'La maison d''Asie'
  , '1 rue Alsace-Lorraine'
  , 'Toulouse'
  , NULL
  , '31000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10501
  , 'BLAUS'
  , 9
  , '1995-05-10'
  , '1995-06-07'
  , '1995-05-17'
  , 3
  , 8.85
  , 'Blauer See Delikatessen'
  , 'Forsterstr. 57'
  , 'Mannheim'
  , NULL
  , '68306'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10502
  , 'PERIC'
  , 2
  , '1995-05-11'
  , '1995-06-08'
  , '1995-05-30'
  , 1
  , 69.32
  , 'Pericles Comidas clásicas'
  , 'Calle Dr. Jorge Cash 321'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10503
  , 'HUNGO'
  , 6
  , '1995-05-12'
  , '1995-06-09'
  , '1995-05-17'
  , 2
  , 16.74
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10504
  , 'WHITC'
  , 4
  , '1995-05-12'
  , '1995-06-09'
  , '1995-05-19'
  , 3
  , 59.13
  , 'White Clover Markets'
  , '1029 - 12th Ave. S.'
  , 'Seattle'
  , 'WA'
  , '98124'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10505
  , 'MEREP'
  , 3
  , '1995-05-15'
  , '1995-06-12'
  , '1995-05-22'
  , 3
  , 7.13
  , 'Mère Paillarde'
  , '43 rue St. Laurent'
  , 'Montréal'
  , 'Québec'
  , 'H1J 1C3'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10506
  , 'KOENE'
  , 9
  , '1995-05-16'
  , '1995-06-13'
  , '1995-06-02'
  , 2
  , 21.19
  , 'Königlich Essen'
  , 'Maubelstr. 90'
  , 'Brandenburg'
  , NULL
  , '14776'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10507
  , 'ANTON'
  , 7
  , '1995-05-16'
  , '1995-06-13'
  , '1995-05-23'
  , 1
  , 47.45
  , 'Antonio Moreno Taquería'
  , 'Mataderos  2312'
  , 'México D.F.'
  , NULL
  , '05023'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10508
  , 'OTTIK'
  , 1
  , '1995-05-17'
  , '1995-06-14'
  , '1995-06-13'
  , 2
  , 4.99
  , 'Ottilies Käseladen'
  , 'Mehrheimerstr. 369'
  , 'Köln'
  , NULL
  , '50739'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10509
  , 'BLAUS'
  , 4
  , '1995-05-18'
  , '1995-06-15'
  , '1995-05-30'
  , 1
  , 0.15
  , 'Blauer See Delikatessen'
  , 'Forsterstr. 57'
  , 'Mannheim'
  , NULL
  , '68306'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10510
  , 'SAVEA'
  , 6
  , '1995-05-19'
  , '1995-06-16'
  , '1995-05-29'
  , 3
  , 367.63
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10511
  , 'BONAP'
  , 4
  , '1995-05-19'
  , '1995-06-16'
  , '1995-05-22'
  , 3
  , 350.64
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10512
  , 'FAMIA'
  , 7
  , '1995-05-22'
  , '1995-06-19'
  , '1995-05-25'
  , 2
  , 3.53
  , 'Familia Arquibaldo'
  , 'Rua Orós, 92'
  , 'São Paulo'
  , 'SP'
  , '05442-030'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10513
  , 'WANDK'
  , 7
  , '1995-05-23'
  , '1995-07-04'
  , '1995-05-29'
  , 1
  , 105.65
  , 'Die Wandernde Kuh'
  , 'Adenauerallee 900'
  , 'Stuttgart'
  , NULL
  , '70563'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10514
  , 'ERNSH'
  , 3
  , '1995-05-23'
  , '1995-06-20'
  , '1995-06-16'
  , 2
  , 789.95
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10515
  , 'QUICK'
  , 2
  , '1995-05-24'
  , '1995-06-07'
  , '1995-06-23'
  , 1
  , 204.47
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10516
  , 'HUNGO'
  , 2
  , '1995-05-25'
  , '1995-06-22'
  , '1995-06-01'
  , 3
  , 62.78
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10517
  , 'NORTS'
  , 3
  , '1995-05-25'
  , '1995-06-22'
  , '1995-05-30'
  , 3
  , 32.07
  , 'North/South'
  , 'South House
300 Queensbridge'
  , 'London'
  , NULL
  , 'SW7 1RZ'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10518
  , 'TORTU'
  , 4
  , '1995-05-26'
  , '1995-06-09'
  , '1995-06-05'
  , 2
  , 218.15
  , 'Tortuga Restaurante'
  , 'Avda. Azteca 123'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10519
  , 'CHOPS'
  , 6
  , '1995-05-29'
  , '1995-06-26'
  , '1995-06-01'
  , 3
  , 91.76
  , 'Chop-suey Chinese'
  , 'Hauptstr. 31'
  , 'Bern'
  , NULL
  , '3012'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10520
  , 'SANTG'
  , 7
  , '1995-05-30'
  , '1995-06-27'
  , '1995-06-01'
  , 1
  , 13.37
  , 'Sant?Gourmet'
  , 'Erling Skakkes gate 78'
  , 'Stavern'
  , NULL
  , '4110'
  , 'Norway'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10521
  , 'CACTU'
  , 8
  , '1995-05-30'
  , '1995-06-27'
  , '1995-06-02'
  , 2
  , 17.22
  , 'Cactus Comidas para llevar'
  , 'Cerrito 333'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10522
  , 'LEHMS'
  , 4
  , '1995-05-31'
  , '1995-06-28'
  , '1995-06-06'
  , 1
  , 45.33
  , 'Lehmanns Marktstand'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10523
  , 'SEVES'
  , 7
  , '1995-06-01'
  , '1995-06-29'
  , '1995-06-30'
  , 2
  , 77.63
  , 'Seven Seas Imports'
  , '90 Wadhurst Rd.'
  , 'London'
  , NULL
  , 'OX15 4NB'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10524
  , 'BERGS'
  , 1
  , '1995-06-01'
  , '1995-06-29'
  , '1995-06-07'
  , 2
  , 244.79
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10525
  , 'BONAP'
  , 1
  , '1995-06-02'
  , '1995-06-30'
  , '1995-06-23'
  , 2
  , 11.06
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10526
  , 'WARTH'
  , 4
  , '1995-06-05'
  , '1995-07-03'
  , '1995-06-15'
  , 2
  , 58.59
  , 'Wartian Herkku'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10527
  , 'QUICK'
  , 7
  , '1995-06-05'
  , '1995-07-03'
  , '1995-06-07'
  , 1
  , 41.90
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10528
  , 'GREAL'
  , 6
  , '1995-06-06'
  , '1995-06-20'
  , '1995-06-09'
  , 2
  , 3.35
  , 'Great Lakes Food Market'
  , '2732 Baker Blvd.'
  , 'Eugene'
  , 'OR'
  , '97403'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10529
  , 'MAISD'
  , 5
  , '1995-06-07'
  , '1995-07-05'
  , '1995-06-09'
  , 2
  , 66.69
  , 'Maison Dewey'
  , 'Rue Joseph-Bens 532'
  , 'Bruxelles'
  , NULL
  , 'B-1180'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10530
  , 'PICCO'
  , 3
  , '1995-06-08'
  , '1995-07-06'
  , '1995-06-12'
  , 2
  , 339.22
  , 'Piccolo und mehr'
  , 'Geislweg 14'
  , 'Salzburg'
  , NULL
  , '5020'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10531
  , 'OCEAN'
  , 7
  , '1995-06-08'
  , '1995-07-06'
  , '1995-06-19'
  , 1
  , 8.12
  , 'Océano Atlántico Ltda.'
  , 'Ing. Gustavo Moncada 8585
Piso 20-A'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10532
  , 'EASTC'
  , 7
  , '1995-06-09'
  , '1995-07-07'
  , '1995-06-12'
  , 3
  , 74.46
  , 'Eastern Connection'
  , '35 King George'
  , 'London'
  , NULL
  , 'WX3 6FW'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10533
  , 'FOLKO'
  , 8
  , '1995-06-12'
  , '1995-07-10'
  , '1995-06-22'
  , 1
  , 188.04
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10534
  , 'LEHMS'
  , 8
  , '1995-06-12'
  , '1995-07-10'
  , '1995-06-14'
  , 2
  , 27.94
  , 'Lehmanns Marktstand'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10535
  , 'ANTON'
  , 4
  , '1995-06-13'
  , '1995-07-11'
  , '1995-06-21'
  , 1
  , 15.64
  , 'Antonio Moreno Taquería'
  , 'Mataderos  2312'
  , 'México D.F.'
  , NULL
  , '05023'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10536
  , 'LEHMS'
  , 3
  , '1995-06-14'
  , '1995-07-12'
  , '1995-07-07'
  , 2
  , 58.88
  , 'Lehmanns Marktstand'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10537
  , 'RICSU'
  , 1
  , '1995-06-14'
  , '1995-06-28'
  , '1995-06-19'
  , 1
  , 78.85
  , 'Richter Supermarkt'
  , 'Starenweg 5'
  , 'Genève'
  , NULL
  , '1204'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10538
  , 'BSBEV'
  , 9
  , '1995-06-15'
  , '1995-07-13'
  , '1995-06-16'
  , 3
  , 4.87
  , 'B''s Beverages'
  , 'Fauntleroy Circus'
  , 'London'
  , NULL
  , 'EC2 5NT'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10539
  , 'BSBEV'
  , 6
  , '1995-06-16'
  , '1995-07-14'
  , '1995-06-23'
  , 3
  , 12.36
  , 'B''s Beverages'
  , 'Fauntleroy Circus'
  , 'London'
  , NULL
  , 'EC2 5NT'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10540
  , 'QUICK'
  , 3
  , '1995-06-19'
  , '1995-07-17'
  , '1995-07-14'
  , 3
  , 1007.64
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10541
  , 'HANAR'
  , 2
  , '1995-06-19'
  , '1995-07-17'
  , '1995-06-29'
  , 1
  , 68.65
  , 'Hanari Carnes'
  , 'Rua do Paço, 67'
  , 'Rio de Janeiro'
  , 'RJ'
  , '05454-876'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10542
  , 'KOENE'
  , 1
  , '1995-06-20'
  , '1995-07-18'
  , '1995-06-26'
  , 3
  , 10.95
  , 'Königlich Essen'
  , 'Maubelstr. 90'
  , 'Brandenburg'
  , NULL
  , '14776'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10543
  , 'LILAS'
  , 8
  , '1995-06-21'
  , '1995-07-19'
  , '1995-06-23'
  , 2
  , 48.17
  , 'LILA-Supermercado'
  , 'Carrera 52 con Ave. Bolívar #65-98 Llano Largo'
  , 'Barquisimeto'
  , 'Lara'
  , '3508'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10544
  , 'LONEP'
  , 4
  , '1995-06-21'
  , '1995-07-19'
  , '1995-06-30'
  , 1
  , 24.91
  , 'Lonesome Pine Restaurant'
  , '89 Chiaroscuro Rd.'
  , 'Portland'
  , 'OR'
  , '97219'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10545
  , 'LAZYK'
  , 8
  , '1995-06-22'
  , '1995-07-20'
  , '1995-07-27'
  , 2
  , 11.92
  , 'Lazy K Kountry Store'
  , '12 Orchestra Terrace'
  , 'Walla Walla'
  , 'WA'
  , '99362'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10546
  , 'VICTE'
  , 1
  , '1995-06-23'
  , '1995-07-21'
  , '1995-06-27'
  , 3
  , 194.72
  , 'Victuailles en stock'
  , '2, rue du Commerce'
  , 'Lyon'
  , NULL
  , '69004'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10547
  , 'SEVES'
  , 3
  , '1995-06-23'
  , '1995-07-21'
  , '1995-07-03'
  , 2
  , 178.43
  , 'Seven Seas Imports'
  , '90 Wadhurst Rd.'
  , 'London'
  , NULL
  , 'OX15 4NB'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10548
  , 'TOMSP'
  , 3
  , '1995-06-26'
  , '1995-07-24'
  , '1995-07-03'
  , 2
  , 1.43
  , 'Toms Spezialitäten'
  , 'Luisenstr. 48'
  , 'Münster'
  , NULL
  , '44087'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10549
  , 'QUICK'
  , 5
  , '1995-06-27'
  , '1995-07-11'
  , '1995-06-30'
  , 1
  , 171.24
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10550
  , 'GODOS'
  , 7
  , '1995-06-28'
  , '1995-07-26'
  , '1995-07-07'
  , 3
  , 4.32
  , 'Godos Cocina Típica'
  , 'C/ Romero, 33'
  , 'Sevilla'
  , NULL
  , '41101'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10551
  , 'FURIB'
  , 4
  , '1995-06-28'
  , '1995-08-09'
  , '1995-07-07'
  , 3
  , 72.95
  , 'Furia Bacalhau e Frutos do Mar'
  , 'Jardim das rosas n. 32'
  , 'Lisboa'
  , NULL
  , '1675'
  , 'Portugal'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10552
  , 'HILAA'
  , 2
  , '1995-06-29'
  , '1995-07-27'
  , '1995-07-06'
  , 1
  , 83.22
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10553
  , 'WARTH'
  , 2
  , '1995-06-30'
  , '1995-07-28'
  , '1995-07-04'
  , 2
  , 149.49
  , 'Wartian Herkku'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10554
  , 'OTTIK'
  , 4
  , '1995-06-30'
  , '1995-07-28'
  , '1995-07-06'
  , 3
  , 120.97
  , 'Ottilies Käseladen'
  , 'Mehrheimerstr. 369'
  , 'Köln'
  , NULL
  , '50739'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10555
  , 'SAVEA'
  , 6
  , '1995-07-03'
  , '1995-07-31'
  , '1995-07-05'
  , 3
  , 252.49
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10556
  , 'SIMOB'
  , 2
  , '1995-07-04'
  , '1995-08-15'
  , '1995-07-14'
  , 1
  , 9.80
  , 'Simons bistro'
  , 'Vinbæltet 34'
  , 'København'
  , NULL
  , '1734'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10557
  , 'LEHMS'
  , 9
  , '1995-07-04'
  , '1995-07-18'
  , '1995-07-07'
  , 2
  , 96.72
  , 'Lehmanns Marktstand'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10558
  , 'AROUT'
  , 1
  , '1995-07-05'
  , '1995-08-02'
  , '1995-07-11'
  , 2
  , 72.97
  , 'Around the Horn'
  , 'Brook Farm
Stratford St. Mary'
  , 'Colchester'
  , 'Essex'
  , 'CO7 6JX'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10559
  , 'BLONP'
  , 6
  , '1995-07-06'
  , '1995-08-03'
  , '1995-07-14'
  , 1
  , 8.05
  , 'Blondel père et fils'
  , '24, place Kléber'
  , 'Strasbourg'
  , NULL
  , '67000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10560
  , 'FRANK'
  , 8
  , '1995-07-07'
  , '1995-08-04'
  , '1995-07-10'
  , 1
  , 36.65
  , 'Frankenversand'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10561
  , 'FOLKO'
  , 2
  , '1995-07-07'
  , '1995-08-04'
  , '1995-07-10'
  , 2
  , 242.21
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10562
  , 'REGGC'
  , 1
  , '1995-07-10'
  , '1995-08-07'
  , '1995-07-13'
  , 1
  , 22.95
  , 'Reggiani Caseifici'
  , 'Strada Provinciale 124'
  , 'Reggio Emilia'
  , NULL
  , '42100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10563
  , 'RICAR'
  , 2
  , '1995-07-11'
  , '1995-08-22'
  , '1995-07-25'
  , 2
  , 60.43
  , 'Ricardo Adocicados'
  , 'Av. Copacabana, 267'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-890'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10564
  , 'RATTC'
  , 4
  , '1995-07-11'
  , '1995-08-08'
  , '1995-07-17'
  , 3
  , 13.75
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10565
  , 'MEREP'
  , 8
  , '1995-07-12'
  , '1995-08-09'
  , '1995-07-19'
  , 2
  , 7.15
  , 'Mère Paillarde'
  , '43 rue St. Laurent'
  , 'Montréal'
  , 'Québec'
  , 'H1J 1C3'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10566
  , 'BLONP'
  , 9
  , '1995-07-13'
  , '1995-08-10'
  , '1995-07-19'
  , 1
  , 88.40
  , 'Blondel père et fils'
  , '24, place Kléber'
  , 'Strasbourg'
  , NULL
  , '67000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10567
  , 'HUNGO'
  , 1
  , '1995-07-13'
  , '1995-08-10'
  , '1995-07-18'
  , 1
  , 33.97
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10568
  , 'GALED'
  , 3
  , '1995-07-14'
  , '1995-08-11'
  , '1995-08-09'
  , 3
  , 6.54
  , 'Galería del gastronómo'
  , 'Rambla de Cataluña, 23'
  , 'Barcelona'
  , NULL
  , '8022'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10569
  , 'RATTC'
  , 5
  , '1995-07-17'
  , '1995-08-14'
  , '1995-08-11'
  , 1
  , 58.98
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10570
  , 'MEREP'
  , 3
  , '1995-07-18'
  , '1995-08-15'
  , '1995-07-20'
  , 3
  , 188.99
  , 'Mère Paillarde'
  , '43 rue St. Laurent'
  , 'Montréal'
  , 'Québec'
  , 'H1J 1C3'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10571
  , 'ERNSH'
  , 8
  , '1995-07-18'
  , '1995-08-29'
  , '1995-08-04'
  , 3
  , 26.06
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10572
  , 'BERGS'
  , 3
  , '1995-07-19'
  , '1995-08-16'
  , '1995-07-26'
  , 2
  , 116.43
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10573
  , 'ANTON'
  , 7
  , '1995-07-20'
  , '1995-08-17'
  , '1995-07-21'
  , 3
  , 84.84
  , 'Antonio Moreno Taquería'
  , 'Mataderos  2312'
  , 'México D.F.'
  , NULL
  , '05023'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10574
  , 'TRAIH'
  , 4
  , '1995-07-20'
  , '1995-08-17'
  , '1995-07-31'
  , 2
  , 37.60
  , 'Trail''s Head Gourmet Provisioners'
  , '722 DaVinci Blvd.'
  , 'Kirkland'
  , 'WA'
  , '98034'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10575
  , 'MORGK'
  , 5
  , '1995-07-21'
  , '1995-08-04'
  , '1995-07-31'
  , 1
  , 127.34
  , 'Morgenstern Gesundkost'
  , 'Heerstr. 22'
  , 'Leipzig'
  , NULL
  , '04179'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10576
  , 'TORTU'
  , 3
  , '1995-07-24'
  , '1995-08-07'
  , '1995-07-31'
  , 3
  , 18.56
  , 'Tortuga Restaurante'
  , 'Avda. Azteca 123'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10577
  , 'TRAIH'
  , 9
  , '1995-07-24'
  , '1995-09-04'
  , '1995-07-31'
  , 2
  , 25.41
  , 'Trail''s Head Gourmet Provisioners'
  , '722 DaVinci Blvd.'
  , 'Kirkland'
  , 'WA'
  , '98034'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10578
  , 'BSBEV'
  , 4
  , '1995-07-25'
  , '1995-08-22'
  , '1995-08-25'
  , 3
  , 29.60
  , 'B''s Beverages'
  , 'Fauntleroy Circus'
  , 'London'
  , NULL
  , 'EC2 5NT'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10579
  , 'LETSS'
  , 1
  , '1995-07-26'
  , '1995-08-23'
  , '1995-08-04'
  , 2
  , 13.73
  , 'Let''s Stop N Shop'
  , '87 Polk St.
Suite 5'
  , 'San Francisco'
  , 'CA'
  , '94117'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10580
  , 'OTTIK'
  , 4
  , '1995-07-27'
  , '1995-08-24'
  , '1995-08-01'
  , 3
  , 75.89
  , 'Ottilies Käseladen'
  , 'Mehrheimerstr. 369'
  , 'Köln'
  , NULL
  , '50739'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10581
  , 'FAMIA'
  , 3
  , '1995-07-27'
  , '1995-08-24'
  , '1995-08-02'
  , 1
  , 3.01
  , 'Familia Arquibaldo'
  , 'Rua Orós, 92'
  , 'São Paulo'
  , 'SP'
  , '05442-030'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10582
  , 'BLAUS'
  , 3
  , '1995-07-28'
  , '1995-08-25'
  , '1995-08-14'
  , 2
  , 27.71
  , 'Blauer See Delikatessen'
  , 'Forsterstr. 57'
  , 'Mannheim'
  , NULL
  , '68306'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10583
  , 'WARTH'
  , 2
  , '1995-07-31'
  , '1995-08-28'
  , '1995-08-04'
  , 2
  , 7.28
  , 'Wartian Herkku'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10584
  , 'BLONP'
  , 4
  , '1995-07-31'
  , '1995-08-28'
  , '1995-08-04'
  , 1
  , 59.14
  , 'Blondel père et fils'
  , '24, place Kléber'
  , 'Strasbourg'
  , NULL
  , '67000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10585
  , 'WELLI'
  , 7
  , '1995-08-01'
  , '1995-08-29'
  , '1995-08-10'
  , 1
  , 13.41
  , 'Wellington Importadora'
  , 'Rua do Mercado, 12'
  , 'Resende'
  , 'SP'
  , '08737-363'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10586
  , 'REGGC'
  , 9
  , '1995-08-02'
  , '1995-08-30'
  , '1995-08-09'
  , 1
  , 0.48
  , 'Reggiani Caseifici'
  , 'Strada Provinciale 124'
  , 'Reggio Emilia'
  , NULL
  , '42100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10587
  , 'QUEDE'
  , 1
  , '1995-08-02'
  , '1995-08-30'
  , '1995-08-09'
  , 1
  , 62.52
  , 'Que Delícia'
  , 'Rua da Panificadora, 12'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-673'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10588
  , 'QUICK'
  , 2
  , '1995-08-03'
  , '1995-08-31'
  , '1995-08-10'
  , 3
  , 194.67
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10589
  , 'GREAL'
  , 8
  , '1995-08-04'
  , '1995-09-01'
  , '1995-08-14'
  , 2
  , 4.42
  , 'Great Lakes Food Market'
  , '2732 Baker Blvd.'
  , 'Eugene'
  , 'OR'
  , '97403'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10590
  , 'MEREP'
  , 4
  , '1995-08-07'
  , '1995-09-04'
  , '1995-08-14'
  , 3
  , 44.77
  , 'Mère Paillarde'
  , '43 rue St. Laurent'
  , 'Montréal'
  , 'Québec'
  , 'H1J 1C3'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10591
  , 'VAFFE'
  , 1
  , '1995-08-07'
  , '1995-08-21'
  , '1995-08-16'
  , 1
  , 55.92
  , 'Vaffeljernet'
  , 'Smagsløget 45'
  , 'Århus'
  , NULL
  , '8200'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10592
  , 'LEHMS'
  , 3
  , '1995-08-08'
  , '1995-09-05'
  , '1995-08-16'
  , 1
  , 32.10
  , 'Lehmanns Marktstand'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10593
  , 'LEHMS'
  , 7
  , '1995-08-09'
  , '1995-09-06'
  , '1995-09-13'
  , 2
  , 174.20
  , 'Lehmanns Marktstand'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10594
  , 'OLDWO'
  , 3
  , '1995-08-09'
  , '1995-09-06'
  , '1995-08-16'
  , 2
  , 5.24
  , 'Old World Delicatessen'
  , '2743 Bering St.'
  , 'Anchorage'
  , 'AK'
  , '99508'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10595
  , 'ERNSH'
  , 2
  , '1995-08-10'
  , '1995-09-07'
  , '1995-08-14'
  , 1
  , 96.78
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10596
  , 'WHITC'
  , 8
  , '1995-08-11'
  , '1995-09-08'
  , '1995-09-12'
  , 1
  , 16.34
  , 'White Clover Markets'
  , '1029 - 12th Ave. S.'
  , 'Seattle'
  , 'WA'
  , '98124'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10597
  , 'PICCO'
  , 7
  , '1995-08-11'
  , '1995-09-08'
  , '1995-08-18'
  , 3
  , 35.12
  , 'Piccolo und mehr'
  , 'Geislweg 14'
  , 'Salzburg'
  , NULL
  , '5020'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10598
  , 'RATTC'
  , 1
  , '1995-08-14'
  , '1995-09-11'
  , '1995-08-18'
  , 3
  , 44.42
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10599
  , 'BSBEV'
  , 6
  , '1995-08-15'
  , '1995-09-26'
  , '1995-08-21'
  , 3
  , 29.98
  , 'B''s Beverages'
  , 'Fauntleroy Circus'
  , 'London'
  , NULL
  , 'EC2 5NT'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10600
  , 'HUNGC'
  , 4
  , '1995-08-16'
  , '1995-09-13'
  , '1995-08-21'
  , 1
  , 45.13
  , 'Hungry Coyote Import Store'
  , 'City Center Plaza
516 Main St.'
  , 'Elgin'
  , 'OR'
  , '97827'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10601
  , 'HILAA'
  , 7
  , '1995-08-16'
  , '1995-09-27'
  , '1995-08-22'
  , 1
  , 58.30
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10602
  , 'VAFFE'
  , 8
  , '1995-08-17'
  , '1995-09-14'
  , '1995-08-22'
  , 2
  , 2.92
  , 'Vaffeljernet'
  , 'Smagsløget 45'
  , 'Århus'
  , NULL
  , '8200'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10603
  , 'SAVEA'
  , 8
  , '1995-08-18'
  , '1995-09-15'
  , '1995-09-08'
  , 2
  , 48.77
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10604
  , 'FURIB'
  , 1
  , '1995-08-18'
  , '1995-09-15'
  , '1995-08-29'
  , 1
  , 7.46
  , 'Furia Bacalhau e Frutos do Mar'
  , 'Jardim das rosas n. 32'
  , 'Lisboa'
  , NULL
  , '1675'
  , 'Portugal'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10605
  , 'MEREP'
  , 1
  , '1995-08-21'
  , '1995-09-18'
  , '1995-08-29'
  , 2
  , 379.13
  , 'Mère Paillarde'
  , '43 rue St. Laurent'
  , 'Montréal'
  , 'Québec'
  , 'H1J 1C3'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10606
  , 'TRADH'
  , 4
  , '1995-08-22'
  , '1995-09-19'
  , '1995-08-31'
  , 3
  , 79.40
  , 'Tradição Hipermercados'
  , 'Av. Inês de Castro, 414'
  , 'São Paulo'
  , 'SP'
  , '05634-030'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10607
  , 'SAVEA'
  , 5
  , '1995-08-22'
  , '1995-09-19'
  , '1995-08-25'
  , 1
  , 200.24
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10608
  , 'TOMSP'
  , 4
  , '1995-08-23'
  , '1995-09-20'
  , '1995-09-01'
  , 2
  , 27.79
  , 'Toms Spezialitäten'
  , 'Luisenstr. 48'
  , 'Münster'
  , NULL
  , '44087'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10609
  , 'DUMON'
  , 7
  , '1995-08-24'
  , '1995-09-21'
  , '1995-08-30'
  , 2
  , 1.85
  , 'Du monde entier'
  , '67, rue des Cinquante Otages'
  , 'Nantes'
  , NULL
  , '44000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10610
  , 'LAMAI'
  , 8
  , '1995-08-25'
  , '1995-09-22'
  , '1995-09-06'
  , 1
  , 26.78
  , 'La maison d''Asie'
  , '1 rue Alsace-Lorraine'
  , 'Toulouse'
  , NULL
  , '31000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10611
  , 'WOLZA'
  , 6
  , '1995-08-25'
  , '1995-09-22'
  , '1995-09-01'
  , 2
  , 80.65
  , 'Wolski Zajazd'
  , 'ul. Filtrowa 68'
  , 'Warszawa'
  , NULL
  , '01-012'
  , 'Poland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10612
  , 'SAVEA'
  , 1
  , '1995-08-28'
  , '1995-09-25'
  , '1995-09-01'
  , 2
  , 544.08
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10613
  , 'HILAA'
  , 4
  , '1995-08-29'
  , '1995-09-26'
  , '1995-09-01'
  , 2
  , 8.11
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10614
  , 'BLAUS'
  , 8
  , '1995-08-29'
  , '1995-09-26'
  , '1995-09-01'
  , 3
  , 1.93
  , 'Blauer See Delikatessen'
  , 'Forsterstr. 57'
  , 'Mannheim'
  , NULL
  , '68306'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10615
  , 'WILMK'
  , 2
  , '1995-08-30'
  , '1995-09-27'
  , '1995-09-06'
  , 3
  , 0.75
  , 'Wilman Kala'
  , 'Keskuskatu 45'
  , 'Helsinki'
  , NULL
  , '21240'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10616
  , 'GREAL'
  , 1
  , '1995-08-31'
  , '1995-09-28'
  , '1995-09-05'
  , 2
  , 116.53
  , 'Great Lakes Food Market'
  , '2732 Baker Blvd.'
  , 'Eugene'
  , 'OR'
  , '97403'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10617
  , 'GREAL'
  , 4
  , '1995-08-31'
  , '1995-09-28'
  , '1995-09-04'
  , 2
  , 18.53
  , 'Great Lakes Food Market'
  , '2732 Baker Blvd.'
  , 'Eugene'
  , 'OR'
  , '97403'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10618
  , 'MEREP'
  , 1
  , '1995-09-01'
  , '1995-10-13'
  , '1995-09-08'
  , 1
  , 154.68
  , 'Mère Paillarde'
  , '43 rue St. Laurent'
  , 'Montréal'
  , 'Québec'
  , 'H1J 1C3'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10619
  , 'MEREP'
  , 3
  , '1995-09-04'
  , '1995-10-02'
  , '1995-09-07'
  , 3
  , 91.05
  , 'Mère Paillarde'
  , '43 rue St. Laurent'
  , 'Montréal'
  , 'Québec'
  , 'H1J 1C3'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10620
  , 'LAUGB'
  , 2
  , '1995-09-05'
  , '1995-10-03'
  , '1995-09-14'
  , 3
  , 0.94
  , 'Laughing Bacchus Wine Cellars'
  , '2319 Elm St.'
  , 'Vancouver'
  , 'BC'
  , 'V3F 2K1'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10621
  , 'ISLAT'
  , 4
  , '1995-09-05'
  , '1995-10-03'
  , '1995-09-11'
  , 2
  , 23.73
  , 'Island Trading'
  , 'Garden House
Crowther Way'
  , 'Cowes'
  , 'Isle of Wight'
  , 'PO31 7PJ'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10622
  , 'RICAR'
  , 4
  , '1995-09-06'
  , '1995-10-04'
  , '1995-09-11'
  , 3
  , 50.97
  , 'Ricardo Adocicados'
  , 'Av. Copacabana, 267'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-890'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10623
  , 'FRANK'
  , 8
  , '1995-09-07'
  , '1995-10-05'
  , '1995-09-12'
  , 2
  , 97.18
  , 'Frankenversand'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10624
  , 'THECR'
  , 4
  , '1995-09-07'
  , '1995-10-05'
  , '1995-09-19'
  , 2
  , 94.80
  , 'The Cracker Box'
  , '55 Grizzly Peak Rd.'
  , 'Butte'
  , 'MT'
  , '59801'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10625
  , 'ANATR'
  , 3
  , '1995-09-08'
  , '1995-10-06'
  , '1995-09-14'
  , 1
  , 43.90
  , 'Ana Trujillo Emparedados y helados'
  , 'Avda. de la Constitución 2222'
  , 'México D.F.'
  , NULL
  , '05021'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10626
  , 'BERGS'
  , 1
  , '1995-09-11'
  , '1995-10-09'
  , '1995-09-20'
  , 2
  , 138.69
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10627
  , 'SAVEA'
  , 8
  , '1995-09-11'
  , '1995-10-23'
  , '1995-09-21'
  , 3
  , 107.46
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10628
  , 'BLONP'
  , 4
  , '1995-09-12'
  , '1995-10-10'
  , '1995-09-20'
  , 3
  , 30.36
  , 'Blondel père et fils'
  , '24, place Kléber'
  , 'Strasbourg'
  , NULL
  , '67000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10629
  , 'GODOS'
  , 4
  , '1995-09-12'
  , '1995-10-10'
  , '1995-09-20'
  , 3
  , 85.46
  , 'Godos Cocina Típica'
  , 'C/ Romero, 33'
  , 'Sevilla'
  , NULL
  , '41101'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10630
  , 'KOENE'
  , 1
  , '1995-09-13'
  , '1995-10-11'
  , '1995-09-19'
  , 2
  , 32.35
  , 'Königlich Essen'
  , 'Maubelstr. 90'
  , 'Brandenburg'
  , NULL
  , '14776'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10631
  , 'LAMAI'
  , 8
  , '1995-09-14'
  , '1995-10-12'
  , '1995-09-15'
  , 1
  , 0.87
  , 'La maison d''Asie'
  , '1 rue Alsace-Lorraine'
  , 'Toulouse'
  , NULL
  , '31000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10632
  , 'WANDK'
  , 8
  , '1995-09-14'
  , '1995-10-12'
  , '1995-09-19'
  , 1
  , 41.38
  , 'Die Wandernde Kuh'
  , 'Adenauerallee 900'
  , 'Stuttgart'
  , NULL
  , '70563'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10633
  , 'ERNSH'
  , 7
  , '1995-09-15'
  , '1995-10-13'
  , '1995-09-18'
  , 3
  , 477.90
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10634
  , 'FOLIG'
  , 4
  , '1995-09-15'
  , '1995-10-13'
  , '1995-09-21'
  , 3
  , 487.38
  , 'Folies gourmandes'
  , '184, chaussée de Tournai'
  , 'Lille'
  , NULL
  , '59000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10635
  , 'MAGAA'
  , 8
  , '1995-09-18'
  , '1995-10-16'
  , '1995-09-21'
  , 3
  , 47.46
  , 'Magazzini Alimentari Riuniti'
  , 'Via Ludovico il Moro 22'
  , 'Bergamo'
  , NULL
  , '24100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10636
  , 'WARTH'
  , 4
  , '1995-09-19'
  , '1995-10-17'
  , '1995-09-26'
  , 1
  , 1.15
  , 'Wartian Herkku'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10637
  , 'QUEEN'
  , 6
  , '1995-09-19'
  , '1995-10-17'
  , '1995-09-26'
  , 1
  , 201.29
  , 'Queen Cozinha'
  , 'Alameda dos Canàrios, 891'
  , 'São Paulo'
  , 'SP'
  , '05487-020'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10638
  , 'LINOD'
  , 3
  , '1995-09-20'
  , '1995-10-18'
  , '1995-10-02'
  , 1
  , 158.44
  , 'LINO-Delicateses'
  , 'Ave. 5 de Mayo Porlamar'
  , 'I. de Margarita'
  , 'Nueva Esparta'
  , '4980'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10639
  , 'SANTG'
  , 7
  , '1995-09-20'
  , '1995-10-18'
  , '1995-09-27'
  , 3
  , 38.64
  , 'Sant?Gourmet'
  , 'Erling Skakkes gate 78'
  , 'Stavern'
  , NULL
  , '4110'
  , 'Norway'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10640
  , 'WANDK'
  , 4
  , '1995-09-21'
  , '1995-10-19'
  , '1995-09-28'
  , 1
  , 23.55
  , 'Die Wandernde Kuh'
  , 'Adenauerallee 900'
  , 'Stuttgart'
  , NULL
  , '70563'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10641
  , 'HILAA'
  , 4
  , '1995-09-22'
  , '1995-10-20'
  , '1995-09-26'
  , 2
  , 179.61
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10642
  , 'SIMOB'
  , 7
  , '1995-09-22'
  , '1995-10-20'
  , '1995-10-06'
  , 3
  , 41.89
  , 'Simons bistro'
  , 'Vinbæltet 34'
  , 'København'
  , NULL
  , '1734'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10643
  , 'ALFKI'
  , 6
  , '1995-09-25'
  , '1995-10-23'
  , '1995-10-03'
  , 1
  , 29.46
  , 'Alfreds Futterkiste'
  , 'Obere Str. 57'
  , 'Berlin'
  , NULL
  , '12209'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10644
  , 'WELLI'
  , 3
  , '1995-09-25'
  , '1995-10-23'
  , '1995-10-02'
  , 2
  , 0.14
  , 'Wellington Importadora'
  , 'Rua do Mercado, 12'
  , 'Resende'
  , 'SP'
  , '08737-363'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10645
  , 'HANAR'
  , 4
  , '1995-09-26'
  , '1995-10-24'
  , '1995-10-03'
  , 1
  , 12.41
  , 'Hanari Carnes'
  , 'Rua do Paço, 67'
  , 'Rio de Janeiro'
  , 'RJ'
  , '05454-876'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10646
  , 'HUNGO'
  , 9
  , '1995-09-27'
  , '1995-11-08'
  , '1995-10-04'
  , 3
  , 142.33
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10647
  , 'QUEDE'
  , 4
  , '1995-09-27'
  , '1995-10-11'
  , '1995-10-04'
  , 2
  , 45.54
  , 'Que Delícia'
  , 'Rua da Panificadora, 12'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-673'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10648
  , 'RICAR'
  , 5
  , '1995-09-28'
  , '1995-11-09'
  , '1995-10-10'
  , 2
  , 14.25
  , 'Ricardo Adocicados'
  , 'Av. Copacabana, 267'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-890'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10649
  , 'MAISD'
  , 5
  , '1995-09-28'
  , '1995-10-26'
  , '1995-09-29'
  , 3
  , 6.20
  , 'Maison Dewey'
  , 'Rue Joseph-Bens 532'
  , 'Bruxelles'
  , NULL
  , 'B-1180'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10650
  , 'FAMIA'
  , 5
  , '1995-09-29'
  , '1995-10-27'
  , '1995-10-04'
  , 3
  , 176.81
  , 'Familia Arquibaldo'
  , 'Rua Orós, 92'
  , 'São Paulo'
  , 'SP'
  , '05442-030'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10651
  , 'WANDK'
  , 8
  , '1995-10-02'
  , '1995-10-30'
  , '1995-10-12'
  , 2
  , 20.60
  , 'Die Wandernde Kuh'
  , 'Adenauerallee 900'
  , 'Stuttgart'
  , NULL
  , '70563'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10652
  , 'GOURL'
  , 4
  , '1995-10-02'
  , '1995-10-30'
  , '1995-10-09'
  , 2
  , 7.14
  , 'Gourmet Lanchonetes'
  , 'Av. Brasil, 442'
  , 'Campinas'
  , 'SP'
  , '04876-786'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10653
  , 'FRANK'
  , 1
  , '1995-10-03'
  , '1995-10-31'
  , '1995-10-20'
  , 1
  , 93.25
  , 'Frankenversand'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10654
  , 'BERGS'
  , 5
  , '1995-10-03'
  , '1995-10-31'
  , '1995-10-12'
  , 1
  , 55.26
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10655
  , 'REGGC'
  , 1
  , '1995-10-04'
  , '1995-11-01'
  , '1995-10-12'
  , 2
  , 4.41
  , 'Reggiani Caseifici'
  , 'Strada Provinciale 124'
  , 'Reggio Emilia'
  , NULL
  , '42100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10656
  , 'GREAL'
  , 6
  , '1995-10-05'
  , '1995-11-02'
  , '1995-10-11'
  , 1
  , 57.15
  , 'Great Lakes Food Market'
  , '2732 Baker Blvd.'
  , 'Eugene'
  , 'OR'
  , '97403'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10657
  , 'SAVEA'
  , 2
  , '1995-10-05'
  , '1995-11-02'
  , '1995-10-16'
  , 2
  , 352.69
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10658
  , 'QUICK'
  , 4
  , '1995-10-06'
  , '1995-11-03'
  , '1995-10-09'
  , 1
  , 364.15
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10659
  , 'QUEEN'
  , 7
  , '1995-10-06'
  , '1995-11-03'
  , '1995-10-11'
  , 2
  , 105.81
  , 'Queen Cozinha'
  , 'Alameda dos Canàrios, 891'
  , 'São Paulo'
  , 'SP'
  , '05487-020'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10660
  , 'HUNGC'
  , 8
  , '1995-10-09'
  , '1995-11-06'
  , '1995-11-15'
  , 1
  , 111.29
  , 'Hungry Coyote Import Store'
  , 'City Center Plaza
516 Main St.'
  , 'Elgin'
  , 'OR'
  , '97827'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10661
  , 'HUNGO'
  , 7
  , '1995-10-10'
  , '1995-11-07'
  , '1995-10-16'
  , 3
  , 17.55
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10662
  , 'LONEP'
  , 3
  , '1995-10-10'
  , '1995-11-07'
  , '1995-10-19'
  , 2
  , 1.28
  , 'Lonesome Pine Restaurant'
  , '89 Chiaroscuro Rd.'
  , 'Portland'
  , 'OR'
  , '97219'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10663
  , 'BONAP'
  , 2
  , '1995-10-11'
  , '1995-10-25'
  , '1995-11-03'
  , 2
  , 113.15
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10664
  , 'FURIB'
  , 1
  , '1995-10-11'
  , '1995-11-08'
  , '1995-10-20'
  , 3
  , 1.27
  , 'Furia Bacalhau e Frutos do Mar'
  , 'Jardim das rosas n. 32'
  , 'Lisboa'
  , NULL
  , '1675'
  , 'Portugal'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10665
  , 'LONEP'
  , 1
  , '1995-10-12'
  , '1995-11-09'
  , '1995-10-18'
  , 2
  , 26.31
  , 'Lonesome Pine Restaurant'
  , '89 Chiaroscuro Rd.'
  , 'Portland'
  , 'OR'
  , '97219'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10666
  , 'RICSU'
  , 7
  , '1995-10-13'
  , '1995-11-10'
  , '1995-10-23'
  , 2
  , 232.42
  , 'Richter Supermarkt'
  , 'Starenweg 5'
  , 'Genève'
  , NULL
  , '1204'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10667
  , 'ERNSH'
  , 7
  , '1995-10-13'
  , '1995-11-10'
  , '1995-10-20'
  , 1
  , 78.09
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10668
  , 'WANDK'
  , 1
  , '1995-10-16'
  , '1995-11-13'
  , '1995-10-24'
  , 2
  , 47.22
  , 'Die Wandernde Kuh'
  , 'Adenauerallee 900'
  , 'Stuttgart'
  , NULL
  , '70563'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10669
  , 'SIMOB'
  , 2
  , '1995-10-16'
  , '1995-11-13'
  , '1995-10-23'
  , 1
  , 24.39
  , 'Simons bistro'
  , 'Vinbæltet 34'
  , 'København'
  , NULL
  , '1734'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10670
  , 'FRANK'
  , 4
  , '1995-10-17'
  , '1995-11-14'
  , '1995-10-19'
  , 1
  , 203.48
  , 'Frankenversand'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10671
  , 'FRANR'
  , 1
  , '1995-10-18'
  , '1995-11-15'
  , '1995-10-25'
  , 1
  , 30.34
  , 'France restauration'
  , '54, rue Royale'
  , 'Nantes'
  , NULL
  , '44000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10672
  , 'BERGS'
  , 9
  , '1995-10-18'
  , '1995-11-01'
  , '1995-10-27'
  , 2
  , 95.75
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10673
  , 'WILMK'
  , 2
  , '1995-10-19'
  , '1995-11-16'
  , '1995-10-20'
  , 1
  , 22.76
  , 'Wilman Kala'
  , 'Keskuskatu 45'
  , 'Helsinki'
  , NULL
  , '21240'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10674
  , 'ISLAT'
  , 4
  , '1995-10-19'
  , '1995-11-16'
  , '1995-10-31'
  , 2
  , 0.90
  , 'Island Trading'
  , 'Garden House
Crowther Way'
  , 'Cowes'
  , 'Isle of Wight'
  , 'PO31 7PJ'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10675
  , 'FRANK'
  , 5
  , '1995-10-20'
  , '1995-11-17'
  , '1995-10-24'
  , 2
  , 31.85
  , 'Frankenversand'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10676
  , 'TORTU'
  , 2
  , '1995-10-23'
  , '1995-11-20'
  , '1995-10-30'
  , 2
  , 2.01
  , 'Tortuga Restaurante'
  , 'Avda. Azteca 123'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10677
  , 'ANTON'
  , 1
  , '1995-10-23'
  , '1995-11-20'
  , '1995-10-27'
  , 3
  , 4.03
  , 'Antonio Moreno Taquería'
  , 'Mataderos  2312'
  , 'México D.F.'
  , NULL
  , '05023'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10678
  , 'SAVEA'
  , 7
  , '1995-10-24'
  , '1995-11-21'
  , '1995-11-16'
  , 3
  , 388.98
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10679
  , 'BLONP'
  , 8
  , '1995-10-24'
  , '1995-11-21'
  , '1995-10-31'
  , 3
  , 27.94
  , 'Blondel père et fils'
  , '24, place Kléber'
  , 'Strasbourg'
  , NULL
  , '67000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10680
  , 'OLDWO'
  , 1
  , '1995-10-25'
  , '1995-11-22'
  , '1995-10-27'
  , 1
  , 26.61
  , 'Old World Delicatessen'
  , '2743 Bering St.'
  , 'Anchorage'
  , 'AK'
  , '99508'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10681
  , 'GREAL'
  , 3
  , '1995-10-26'
  , '1995-11-23'
  , '1995-10-31'
  , 3
  , 76.13
  , 'Great Lakes Food Market'
  , '2732 Baker Blvd.'
  , 'Eugene'
  , 'OR'
  , '97403'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10682
  , 'ANTON'
  , 3
  , '1995-10-26'
  , '1995-11-23'
  , '1995-11-01'
  , 2
  , 36.13
  , 'Antonio Moreno Taquería'
  , 'Mataderos  2312'
  , 'México D.F.'
  , NULL
  , '05023'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10683
  , 'DUMON'
  , 2
  , '1995-10-27'
  , '1995-11-24'
  , '1995-11-01'
  , 1
  , 4.40
  , 'Du monde entier'
  , '67, rue des Cinquante Otages'
  , 'Nantes'
  , NULL
  , '44000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10684
  , 'OTTIK'
  , 3
  , '1995-10-27'
  , '1995-11-24'
  , '1995-10-31'
  , 1
  , 145.63
  , 'Ottilies Käseladen'
  , 'Mehrheimerstr. 369'
  , 'Köln'
  , NULL
  , '50739'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10685
  , 'GOURL'
  , 4
  , '1995-10-30'
  , '1995-11-13'
  , '1995-11-03'
  , 2
  , 33.75
  , 'Gourmet Lanchonetes'
  , 'Av. Brasil, 442'
  , 'Campinas'
  , 'SP'
  , '04876-786'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10686
  , 'PICCO'
  , 2
  , '1995-10-31'
  , '1995-11-28'
  , '1995-11-08'
  , 1
  , 96.50
  , 'Piccolo und mehr'
  , 'Geislweg 14'
  , 'Salzburg'
  , NULL
  , '5020'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10687
  , 'HUNGO'
  , 9
  , '1995-10-31'
  , '1995-11-28'
  , '1995-11-30'
  , 2
  , 296.43
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10688
  , 'VAFFE'
  , 4
  , '1995-11-01'
  , '1995-11-15'
  , '1995-11-07'
  , 2
  , 299.09
  , 'Vaffeljernet'
  , 'Smagsløget 45'
  , 'Århus'
  , NULL
  , '8200'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10689
  , 'BERGS'
  , 1
  , '1995-11-01'
  , '1995-11-29'
  , '1995-11-07'
  , 2
  , 13.42
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10690
  , 'HANAR'
  , 1
  , '1995-11-02'
  , '1995-11-30'
  , '1995-11-03'
  , 1
  , 15.80
  , 'Hanari Carnes'
  , 'Rua do Paço, 67'
  , 'Rio de Janeiro'
  , 'RJ'
  , '05454-876'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10691
  , 'QUICK'
  , 2
  , '1995-11-03'
  , '1995-12-15'
  , '1995-11-22'
  , 2
  , 810.05
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10692
  , 'ALFKI'
  , 4
  , '1995-11-03'
  , '1995-12-01'
  , '1995-11-13'
  , 2
  , 61.02
  , 'Alfred''s Futterkiste'
  , 'Obere Str. 57'
  , 'Berlin'
  , NULL
  , '12209'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10693
  , 'WHITC'
  , 3
  , '1995-11-06'
  , '1995-11-20'
  , '1995-11-10'
  , 3
  , 139.34
  , 'White Clover Markets'
  , '1029 - 12th Ave. S.'
  , 'Seattle'
  , 'WA'
  , '98124'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10694
  , 'QUICK'
  , 8
  , '1995-11-06'
  , '1995-12-04'
  , '1995-11-09'
  , 3
  , 398.36
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10695
  , 'WILMK'
  , 7
  , '1995-11-07'
  , '1995-12-19'
  , '1995-11-14'
  , 1
  , 16.72
  , 'Wilman Kala'
  , 'Keskuskatu 45'
  , 'Helsinki'
  , NULL
  , '21240'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10696
  , 'WHITC'
  , 8
  , '1995-11-08'
  , '1995-12-20'
  , '1995-11-14'
  , 3
  , 102.55
  , 'White Clover Markets'
  , '1029 - 12th Ave. S.'
  , 'Seattle'
  , 'WA'
  , '98124'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10697
  , 'LINOD'
  , 3
  , '1995-11-08'
  , '1995-12-06'
  , '1995-11-14'
  , 1
  , 45.52
  , 'LINO-Delicateses'
  , 'Ave. 5 de Mayo Porlamar'
  , 'I. de Margarita'
  , 'Nueva Esparta'
  , '4980'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10698
  , 'ERNSH'
  , 4
  , '1995-11-09'
  , '1995-12-07'
  , '1995-11-17'
  , 1
  , 272.47
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10699
  , 'MORGK'
  , 3
  , '1995-11-09'
  , '1995-12-07'
  , '1995-11-13'
  , 3
  , 0.58
  , 'Morgenstern Gesundkost'
  , 'Heerstr. 22'
  , 'Leipzig'
  , NULL
  , '04179'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10700
  , 'SAVEA'
  , 3
  , '1995-11-10'
  , '1995-12-08'
  , '1995-11-16'
  , 1
  , 65.10
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10701
  , 'HUNGO'
  , 6
  , '1995-11-13'
  , '1995-11-27'
  , '1995-11-15'
  , 3
  , 220.31
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10702
  , 'ALFKI'
  , 4
  , '1995-11-13'
  , '1995-12-25'
  , '1995-11-21'
  , 1
  , 23.94
  , 'Alfred''s Futterkiste'
  , 'Obere Str. 57'
  , 'Berlin'
  , NULL
  , '12209'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10703
  , 'FOLKO'
  , 6
  , '1995-11-14'
  , '1995-12-12'
  , '1995-11-20'
  , 2
  , 152.30
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10704
  , 'QUEEN'
  , 6
  , '1995-11-14'
  , '1995-12-12'
  , '1995-12-08'
  , 1
  , 4.78
  , 'Queen Cozinha'
  , 'Alameda dos Canàrios, 891'
  , 'São Paulo'
  , 'SP'
  , '05487-020'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10705
  , 'HILAA'
  , 9
  , '1995-11-15'
  , '1995-12-13'
  , '1995-12-19'
  , 2
  , 3.52
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10706
  , 'OLDWO'
  , 8
  , '1995-11-16'
  , '1995-12-14'
  , '1995-11-21'
  , 3
  , 135.63
  , 'Old World Delicatessen'
  , '2743 Bering St.'
  , 'Anchorage'
  , 'AK'
  , '99508'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10707
  , 'AROUT'
  , 4
  , '1995-11-16'
  , '1995-11-30'
  , '1995-11-23'
  , 3
  , 21.74
  , 'Around the Horn'
  , 'Brook Farm
Stratford St. Mary'
  , 'Colchester'
  , 'Essex'
  , 'CO7 6JX'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10708
  , 'THEBI'
  , 6
  , '1995-11-17'
  , '1995-12-29'
  , '1995-12-06'
  , 2
  , 2.96
  , 'The Big Cheese'
  , '89 Jefferson Way
Suite 2'
  , 'Portland'
  , 'OR'
  , '97201'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10709
  , 'GOURL'
  , 1
  , '1995-11-17'
  , '1995-12-15'
  , '1995-12-21'
  , 3
  , 210.80
  , 'Gourmet Lanchonetes'
  , 'Av. Brasil, 442'
  , 'Campinas'
  , 'SP'
  , '04876-786'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10710
  , 'FRANS'
  , 1
  , '1995-11-20'
  , '1995-12-18'
  , '1995-11-23'
  , 1
  , 4.98
  , 'Franchi S.p.A.'
  , 'Via Monte Bianco 34'
  , 'Torino'
  , NULL
  , '10100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10711
  , 'SAVEA'
  , 5
  , '1995-11-21'
  , '1996-01-02'
  , '1995-11-29'
  , 2
  , 52.41
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10712
  , 'HUNGO'
  , 3
  , '1995-11-21'
  , '1995-12-19'
  , '1995-12-01'
  , 1
  , 89.93
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10713
  , 'SAVEA'
  , 1
  , '1995-11-22'
  , '1995-12-20'
  , '1995-11-24'
  , 1
  , 167.05
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10714
  , 'SAVEA'
  , 5
  , '1995-11-22'
  , '1995-12-20'
  , '1995-11-27'
  , 3
  , 24.49
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10715
  , 'BONAP'
  , 3
  , '1995-11-23'
  , '1995-12-07'
  , '1995-11-29'
  , 1
  , 63.20
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10716
  , 'RANCH'
  , 4
  , '1995-11-24'
  , '1995-12-22'
  , '1995-11-27'
  , 2
  , 22.57
  , 'Rancho grande'
  , 'Av. del Libertador 900'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10717
  , 'FRANK'
  , 1
  , '1995-11-24'
  , '1995-12-22'
  , '1995-11-29'
  , 2
  , 59.25
  , 'Frankenversand'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10718
  , 'KOENE'
  , 1
  , '1995-11-27'
  , '1995-12-25'
  , '1995-11-29'
  , 3
  , 170.88
  , 'Königlich Essen'
  , 'Maubelstr. 90'
  , 'Brandenburg'
  , NULL
  , '14776'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10719
  , 'LETSS'
  , 8
  , '1995-11-27'
  , '1995-12-25'
  , '1995-12-06'
  , 2
  , 51.44
  , 'Let''s Stop N Shop'
  , '87 Polk St.
Suite 5'
  , 'San Francisco'
  , 'CA'
  , '94117'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10720
  , 'QUEDE'
  , 8
  , '1995-11-28'
  , '1995-12-12'
  , '1995-12-06'
  , 2
  , 9.53
  , 'Que Delícia'
  , 'Rua da Panificadora, 12'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-673'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10721
  , 'QUICK'
  , 5
  , '1995-11-29'
  , '1995-12-27'
  , '1995-12-01'
  , 3
  , 48.92
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10722
  , 'SAVEA'
  , 8
  , '1995-11-29'
  , '1996-01-10'
  , '1995-12-05'
  , 1
  , 74.58
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10723
  , 'WHITC'
  , 3
  , '1995-11-30'
  , '1995-12-28'
  , '1995-12-26'
  , 1
  , 21.72
  , 'White Clover Markets'
  , '1029 - 12th Ave. S.'
  , 'Seattle'
  , 'WA'
  , '98124'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10724
  , 'MEREP'
  , 8
  , '1995-11-30'
  , '1996-01-11'
  , '1995-12-06'
  , 2
  , 57.75
  , 'Mère Paillarde'
  , '43 rue St. Laurent'
  , 'Montréal'
  , 'Québec'
  , 'H1J 1C3'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10725
  , 'FAMIA'
  , 4
  , '1995-12-01'
  , '1995-12-29'
  , '1995-12-06'
  , 3
  , 10.83
  , 'Familia Arquibaldo'
  , 'Rua Orós, 92'
  , 'São Paulo'
  , 'SP'
  , '05442-030'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10726
  , 'EASTC'
  , 4
  , '1995-12-04'
  , '1995-12-18'
  , '1996-01-05'
  , 1
  , 16.56
  , 'Eastern Connection'
  , '35 King George'
  , 'London'
  , NULL
  , 'WX3 6FW'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10727
  , 'REGGC'
  , 2
  , '1995-12-04'
  , '1996-01-01'
  , '1996-01-05'
  , 1
  , 89.90
  , 'Reggiani Caseifici'
  , 'Strada Provinciale 124'
  , 'Reggio Emilia'
  , NULL
  , '42100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10728
  , 'QUEEN'
  , 4
  , '1995-12-05'
  , '1996-01-02'
  , '1995-12-12'
  , 2
  , 58.33
  , 'Queen Cozinha'
  , 'Alameda dos Canàrios, 891'
  , 'São Paulo'
  , 'SP'
  , '05487-020'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10729
  , 'LINOD'
  , 8
  , '1995-12-05'
  , '1996-01-16'
  , '1995-12-15'
  , 3
  , 141.06
  , 'LINO-Delicateses'
  , 'Ave. 5 de Mayo Porlamar'
  , 'I. de Margarita'
  , 'Nueva Esparta'
  , '4980'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10730
  , 'BONAP'
  , 5
  , '1995-12-06'
  , '1996-01-03'
  , '1995-12-15'
  , 1
  , 20.12
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10731
  , 'CHOPS'
  , 7
  , '1995-12-07'
  , '1996-01-04'
  , '1995-12-15'
  , 1
  , 96.65
  , 'Chop-suey Chinese'
  , 'Hauptstr. 31'
  , 'Bern'
  , NULL
  , '3012'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10732
  , 'BONAP'
  , 3
  , '1995-12-07'
  , '1996-01-04'
  , '1995-12-08'
  , 1
  , 16.97
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10733
  , 'BERGS'
  , 1
  , '1995-12-08'
  , '1996-01-05'
  , '1995-12-11'
  , 3
  , 110.11
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10734
  , 'GOURL'
  , 2
  , '1995-12-08'
  , '1996-01-05'
  , '1995-12-13'
  , 3
  , 1.63
  , 'Gourmet Lanchonetes'
  , 'Av. Brasil, 442'
  , 'Campinas'
  , 'SP'
  , '04876-786'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10735
  , 'LETSS'
  , 6
  , '1995-12-11'
  , '1996-01-08'
  , '1995-12-22'
  , 2
  , 45.97
  , 'Let''s Stop N Shop'
  , '87 Polk St.
Suite 5'
  , 'San Francisco'
  , 'CA'
  , '94117'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10736
  , 'HUNGO'
  , 9
  , '1995-12-12'
  , '1996-01-09'
  , '1995-12-22'
  , 2
  , 44.10
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10737
  , 'VINET'
  , 2
  , '1995-12-12'
  , '1996-01-09'
  , '1995-12-19'
  , 2
  , 7.79
  , 'Vins et alcools Chevalier'
  , '59 rue de l''Abbaye'
  , 'Reims'
  , NULL
  , '51100'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10738
  , 'SPECD'
  , 2
  , '1995-12-13'
  , '1996-01-10'
  , '1995-12-19'
  , 1
  , 2.91
  , 'Spécialités du monde'
  , '25, rue Lauriston'
  , 'Paris'
  , NULL
  , '75016'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10739
  , 'VINET'
  , 3
  , '1995-12-13'
  , '1996-01-10'
  , '1995-12-18'
  , 3
  , 11.08
  , 'Vins et alcools Chevalier'
  , '59 rue de l''Abbaye'
  , 'Reims'
  , NULL
  , '51100'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10740
  , 'WHITC'
  , 4
  , '1995-12-14'
  , '1996-01-11'
  , '1995-12-26'
  , 2
  , 81.88
  , 'White Clover Markets'
  , '1029 - 12th Ave. S.'
  , 'Seattle'
  , 'WA'
  , '98124'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10741
  , 'AROUT'
  , 4
  , '1995-12-15'
  , '1995-12-29'
  , '1995-12-19'
  , 3
  , 10.96
  , 'Around the Horn'
  , 'Brook Farm
Stratford St. Mary'
  , 'Colchester'
  , 'Essex'
  , 'CO7 6JX'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10742
  , 'BOTTM'
  , 3
  , '1995-12-15'
  , '1996-01-12'
  , '1995-12-19'
  , 3
  , 243.73
  , 'Bottom-Dollar Markets'
  , '23 Tsawassen Blvd.'
  , 'Tsawassen'
  , 'BC'
  , 'T2F 8M4'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10743
  , 'AROUT'
  , 1
  , '1995-12-18'
  , '1996-01-15'
  , '1995-12-22'
  , 2
  , 23.72
  , 'Around the Horn'
  , 'Brook Farm
Stratford St. Mary'
  , 'Colchester'
  , 'Essex'
  , 'CO7 6JX'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10744
  , 'VAFFE'
  , 6
  , '1995-12-18'
  , '1996-01-15'
  , '1995-12-25'
  , 1
  , 69.19
  , 'Vaffeljernet'
  , 'Smagsløget 45'
  , 'Århus'
  , NULL
  , '8200'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10745
  , 'QUICK'
  , 9
  , '1995-12-19'
  , '1996-01-16'
  , '1995-12-28'
  , 1
  , 3.52
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10746
  , 'CHOPS'
  , 1
  , '1995-12-20'
  , '1996-01-17'
  , '1995-12-22'
  , 3
  , 31.43
  , 'Chop-suey Chinese'
  , 'Hauptstr. 31'
  , 'Bern'
  , NULL
  , '3012'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10747
  , 'PICCO'
  , 6
  , '1995-12-20'
  , '1996-01-17'
  , '1995-12-27'
  , 1
  , 117.33
  , 'Piccolo und mehr'
  , 'Geislweg 14'
  , 'Salzburg'
  , NULL
  , '5020'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10748
  , 'SAVEA'
  , 3
  , '1995-12-21'
  , '1996-01-18'
  , '1995-12-29'
  , 1
  , 232.55
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10749
  , 'ISLAT'
  , 4
  , '1995-12-21'
  , '1996-01-18'
  , '1996-01-19'
  , 2
  , 61.53
  , 'Island Trading'
  , 'Garden House
Crowther Way'
  , 'Cowes'
  , 'Isle of Wight'
  , 'PO31 7PJ'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10750
  , 'WARTH'
  , 9
  , '1995-12-22'
  , '1996-01-19'
  , '1995-12-25'
  , 1
  , 79.30
  , 'Wartian Herkku'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10751
  , 'RICSU'
  , 3
  , '1995-12-25'
  , '1996-01-22'
  , '1996-01-03'
  , 3
  , 130.79
  , 'Richter Supermarkt'
  , 'Starenweg 5'
  , 'Genève'
  , NULL
  , '1204'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10752
  , 'NORTS'
  , 2
  , '1995-12-25'
  , '1996-01-22'
  , '1995-12-29'
  , 3
  , 1.39
  , 'North/South'
  , 'South House
300 Queensbridge'
  , 'London'
  , NULL
  , 'SW7 1RZ'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10753
  , 'FRANS'
  , 3
  , '1995-12-26'
  , '1996-01-23'
  , '1995-12-28'
  , 1
  , 7.70
  , 'Franchi S.p.A.'
  , 'Via Monte Bianco 34'
  , 'Torino'
  , NULL
  , '10100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10754
  , 'MAGAA'
  , 6
  , '1995-12-26'
  , '1996-01-23'
  , '1995-12-28'
  , 3
  , 2.38
  , 'Magazzini Alimentari Riuniti'
  , 'Via Ludovico il Moro 22'
  , 'Bergamo'
  , NULL
  , '24100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10755
  , 'BONAP'
  , 4
  , '1995-12-27'
  , '1996-01-24'
  , '1995-12-29'
  , 2
  , 16.71
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10756
  , 'SPLIR'
  , 8
  , '1995-12-28'
  , '1996-01-25'
  , '1996-01-02'
  , 2
  , 73.21
  , 'Split Rail Beer & Ale'
  , 'P.O. Box 555'
  , 'Lander'
  , 'WY'
  , '82520'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10757
  , 'SAVEA'
  , 6
  , '1995-12-28'
  , '1996-01-25'
  , '1996-01-15'
  , 1
  , 8.19
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10758
  , 'RICSU'
  , 3
  , '1995-12-29'
  , '1996-01-26'
  , '1996-01-04'
  , 3
  , 138.17
  , 'Richter Supermarkt'
  , 'Starenweg 5'
  , 'Genève'
  , NULL
  , '1204'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10759
  , 'ANATR'
  , 3
  , '1995-12-29'
  , '1996-01-26'
  , '1996-01-12'
  , 3
  , 11.99
  , 'Ana Trujillo Emparedados y helados'
  , 'Avda. de la Constitución 2222'
  , 'México D.F.'
  , NULL
  , '05021'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10760
  , 'MAISD'
  , 4
  , '1996-01-01'
  , '1996-01-29'
  , '1996-01-10'
  , 1
  , 155.64
  , 'Maison Dewey'
  , 'Rue Joseph-Bens 532'
  , 'Bruxelles'
  , NULL
  , 'B-1180'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10761
  , 'RATTC'
  , 5
  , '1996-01-02'
  , '1996-01-30'
  , '1996-01-08'
  , 2
  , 18.66
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10762
  , 'FOLKO'
  , 3
  , '1996-01-02'
  , '1996-01-30'
  , '1996-01-09'
  , 1
  , 328.74
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10763
  , 'FOLIG'
  , 3
  , '1996-01-03'
  , '1996-01-31'
  , '1996-01-08'
  , 3
  , 37.35
  , 'Folies gourmandes'
  , '184, chaussée de Tournai'
  , 'Lille'
  , NULL
  , '59000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10764
  , 'ERNSH'
  , 6
  , '1996-01-03'
  , '1996-01-31'
  , '1996-01-08'
  , 3
  , 145.45
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10765
  , 'QUICK'
  , 3
  , '1996-01-04'
  , '1996-02-01'
  , '1996-01-09'
  , 3
  , 42.74
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10766
  , 'OTTIK'
  , 4
  , '1996-01-05'
  , '1996-02-02'
  , '1996-01-09'
  , 1
  , 157.55
  , 'Ottilies Käseladen'
  , 'Mehrheimerstr. 369'
  , 'Köln'
  , NULL
  , '50739'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10767
  , 'SUPRD'
  , 4
  , '1996-01-05'
  , '1996-02-02'
  , '1996-01-15'
  , 3
  , 1.59
  , 'Suprêmes délices'
  , 'Boulevard Tirou, 255'
  , 'Charleroi'
  , NULL
  , 'B-6000'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10768
  , 'AROUT'
  , 3
  , '1996-01-08'
  , '1996-02-05'
  , '1996-01-15'
  , 2
  , 146.32
  , 'Around the Horn'
  , 'Brook Farm
Stratford St. Mary'
  , 'Colchester'
  , 'Essex'
  , 'CO7 6JX'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10769
  , 'VAFFE'
  , 3
  , '1996-01-08'
  , '1996-02-05'
  , '1996-01-12'
  , 1
  , 65.06
  , 'Vaffeljernet'
  , 'Smagsløget 45'
  , 'Århus'
  , NULL
  , '8200'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10770
  , 'HANAR'
  , 8
  , '1996-01-09'
  , '1996-02-06'
  , '1996-01-17'
  , 3
  , 5.32
  , 'Hanari Carnes'
  , 'Rua do Paço, 67'
  , 'Rio de Janeiro'
  , 'RJ'
  , '05454-876'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10771
  , 'ERNSH'
  , 9
  , '1996-01-10'
  , '1996-02-07'
  , '1996-02-02'
  , 2
  , 11.19
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10772
  , 'LEHMS'
  , 3
  , '1996-01-10'
  , '1996-02-07'
  , '1996-01-19'
  , 2
  , 91.28
  , 'Lehmanns Marktstand'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10773
  , 'ERNSH'
  , 1
  , '1996-01-11'
  , '1996-02-08'
  , '1996-01-16'
  , 3
  , 96.43
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10774
  , 'FOLKO'
  , 4
  , '1996-01-11'
  , '1996-01-25'
  , '1996-01-12'
  , 1
  , 48.20
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10775
  , 'THECR'
  , 7
  , '1996-01-12'
  , '1996-02-09'
  , '1996-01-26'
  , 1
  , 20.25
  , 'The Cracker Box'
  , '55 Grizzly Peak Rd.'
  , 'Butte'
  , 'MT'
  , '59801'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10776
  , 'ERNSH'
  , 1
  , '1996-01-15'
  , '1996-02-12'
  , '1996-01-18'
  , 3
  , 351.53
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10777
  , 'GOURL'
  , 7
  , '1996-01-15'
  , '1996-01-29'
  , '1996-02-21'
  , 2
  , 3.01
  , 'Gourmet Lanchonetes'
  , 'Av. Brasil, 442'
  , 'Campinas'
  , 'SP'
  , '04876-786'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10778
  , 'BERGS'
  , 3
  , '1996-01-16'
  , '1996-02-13'
  , '1996-01-24'
  , 1
  , 6.79
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10779
  , 'MORGK'
  , 3
  , '1996-01-16'
  , '1996-02-13'
  , '1996-02-14'
  , 2
  , 58.13
  , 'Morgenstern Gesundkost'
  , 'Heerstr. 22'
  , 'Leipzig'
  , NULL
  , '04179'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10780
  , 'LILAS'
  , 2
  , '1996-01-16'
  , '1996-01-30'
  , '1996-01-25'
  , 1
  , 42.13
  , 'LILA-Supermercado'
  , 'Carrera 52 con Ave. Bolívar #65-98 Llano Largo'
  , 'Barquisimeto'
  , 'Lara'
  , '3508'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10781
  , 'WARTH'
  , 2
  , '1996-01-17'
  , '1996-02-14'
  , '1996-01-19'
  , 3
  , 73.16
  , 'Wartian Herkku'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10782
  , 'CACTU'
  , 9
  , '1996-01-17'
  , '1996-02-14'
  , '1996-01-22'
  , 3
  , 1.10
  , 'Cactus Comidas para llevar'
  , 'Cerrito 333'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10783
  , 'HANAR'
  , 4
  , '1996-01-18'
  , '1996-02-15'
  , '1996-01-19'
  , 2
  , 124.98
  , 'Hanari Carnes'
  , 'Rua do Paço, 67'
  , 'Rio de Janeiro'
  , 'RJ'
  , '05454-876'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10784
  , 'MAGAA'
  , 4
  , '1996-01-18'
  , '1996-02-15'
  , '1996-01-22'
  , 3
  , 70.09
  , 'Magazzini Alimentari Riuniti'
  , 'Via Ludovico il Moro 22'
  , 'Bergamo'
  , NULL
  , '24100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10785
  , 'GROSR'
  , 1
  , '1996-01-18'
  , '1996-02-15'
  , '1996-01-24'
  , 3
  , 1.51
  , 'GROSELLA-Restaurante'
  , '5?Ave. Los Palos Grandes'
  , 'Caracas'
  , 'DF'
  , '1081'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10786
  , 'QUEEN'
  , 8
  , '1996-01-19'
  , '1996-02-16'
  , '1996-01-23'
  , 1
  , 110.87
  , 'Queen Cozinha'
  , 'Alameda dos Canàrios, 891'
  , 'São Paulo'
  , 'SP'
  , '05487-020'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10787
  , 'LAMAI'
  , 2
  , '1996-01-19'
  , '1996-02-02'
  , '1996-01-26'
  , 1
  , 249.93
  , 'La maison d''Asie'
  , '1 rue Alsace-Lorraine'
  , 'Toulouse'
  , NULL
  , '31000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10788
  , 'QUICK'
  , 1
  , '1996-01-22'
  , '1996-02-19'
  , '1996-02-19'
  , 2
  , 42.70
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10789
  , 'FOLIG'
  , 1
  , '1996-01-22'
  , '1996-02-19'
  , '1996-01-31'
  , 2
  , 100.60
  , 'Folies gourmandes'
  , '184, chaussée de Tournai'
  , 'Lille'
  , NULL
  , '59000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10790
  , 'GOURL'
  , 6
  , '1996-01-22'
  , '1996-02-19'
  , '1996-01-26'
  , 1
  , 28.23
  , 'Gourmet Lanchonetes'
  , 'Av. Brasil, 442'
  , 'Campinas'
  , 'SP'
  , '04876-786'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10791
  , 'FRANK'
  , 6
  , '1996-01-23'
  , '1996-02-20'
  , '1996-02-01'
  , 2
  , 16.85
  , 'Frankenversand'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10792
  , 'WOLZA'
  , 1
  , '1996-01-23'
  , '1996-02-20'
  , '1996-01-31'
  , 3
  , 23.79
  , 'Wolski Zajazd'
  , 'ul. Filtrowa 68'
  , 'Warszawa'
  , NULL
  , '01-012'
  , 'Poland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10793
  , 'AROUT'
  , 3
  , '1996-01-24'
  , '1996-02-21'
  , '1996-02-08'
  , 3
  , 4.52
  , 'Around the Horn'
  , 'Brook Farm
Stratford St. Mary'
  , 'Colchester'
  , 'Essex'
  , 'CO7 6JX'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10794
  , 'QUEDE'
  , 6
  , '1996-01-24'
  , '1996-02-21'
  , '1996-02-02'
  , 1
  , 21.49
  , 'Que Delícia'
  , 'Rua da Panificadora, 12'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-673'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10795
  , 'ERNSH'
  , 8
  , '1996-01-24'
  , '1996-02-21'
  , '1996-02-20'
  , 2
  , 126.66
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10796
  , 'HILAA'
  , 3
  , '1996-01-25'
  , '1996-02-22'
  , '1996-02-14'
  , 1
  , 26.52
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10797
  , 'DRACD'
  , 7
  , '1996-01-25'
  , '1996-02-22'
  , '1996-02-05'
  , 2
  , 33.35
  , 'Drachenblut Delikatessen'
  , 'Walserweg 21'
  , 'Aachen'
  , NULL
  , '52066'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10798
  , 'ISLAT'
  , 2
  , '1996-01-26'
  , '1996-02-23'
  , '1996-02-05'
  , 1
  , 2.33
  , 'Island Trading'
  , 'Garden House
Crowther Way'
  , 'Cowes'
  , 'Isle of Wight'
  , 'PO31 7PJ'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10799
  , 'KOENE'
  , 9
  , '1996-01-26'
  , '1996-03-08'
  , '1996-02-05'
  , 3
  , 30.76
  , 'Königlich Essen'
  , 'Maubelstr. 90'
  , 'Brandenburg'
  , NULL
  , '14776'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10800
  , 'SEVES'
  , 1
  , '1996-01-26'
  , '1996-02-23'
  , '1996-02-05'
  , 3
  , 137.44
  , 'Seven Seas Imports'
  , '90 Wadhurst Rd.'
  , 'London'
  , NULL
  , 'OX15 4NB'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10801
  , 'BOLID'
  , 4
  , '1996-01-29'
  , '1996-02-26'
  , '1996-01-31'
  , 2
  , 97.09
  , 'Bólido Comidas preparadas'
  , 'C/ Araquil, 67'
  , 'Madrid'
  , NULL
  , '28023'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10802
  , 'SIMOB'
  , 4
  , '1996-01-29'
  , '1996-02-26'
  , '1996-02-02'
  , 2
  , 257.26
  , 'Simons bistro'
  , 'Vinbæltet 34'
  , 'København'
  , NULL
  , '1734'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10803
  , 'WELLI'
  , 4
  , '1996-01-30'
  , '1996-02-27'
  , '1996-02-06'
  , 1
  , 55.23
  , 'Wellington Importadora'
  , 'Rua do Mercado, 12'
  , 'Resende'
  , 'SP'
  , '08737-363'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10804
  , 'SEVES'
  , 6
  , '1996-01-30'
  , '1996-02-27'
  , '1996-02-07'
  , 2
  , 27.33
  , 'Seven Seas Imports'
  , '90 Wadhurst Rd.'
  , 'London'
  , NULL
  , 'OX15 4NB'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10805
  , 'THEBI'
  , 2
  , '1996-01-30'
  , '1996-02-27'
  , '1996-02-09'
  , 3
  , 237.34
  , 'The Big Cheese'
  , '89 Jefferson Way
Suite 2'
  , 'Portland'
  , 'OR'
  , '97201'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10806
  , 'VICTE'
  , 3
  , '1996-01-31'
  , '1996-02-28'
  , '1996-02-05'
  , 2
  , 22.11
  , 'Victuailles en stock'
  , '2, rue du Commerce'
  , 'Lyon'
  , NULL
  , '69004'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10807
  , 'FRANS'
  , 4
  , '1996-01-31'
  , '1996-02-28'
  , '1996-03-01'
  , 1
  , 1.36
  , 'Franchi S.p.A.'
  , 'Via Monte Bianco 34'
  , 'Torino'
  , NULL
  , '10100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10808
  , 'OLDWO'
  , 2
  , '1996-02-01'
  , '1996-02-29'
  , '1996-02-09'
  , 3
  , 45.53
  , 'Old World Delicatessen'
  , '2743 Bering St.'
  , 'Anchorage'
  , 'AK'
  , '99508'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10809
  , 'WELLI'
  , 7
  , '1996-02-01'
  , '1996-02-29'
  , '1996-02-07'
  , 1
  , 4.87
  , 'Wellington Importadora'
  , 'Rua do Mercado, 12'
  , 'Resende'
  , 'SP'
  , '08737-363'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10810
  , 'LAUGB'
  , 2
  , '1996-02-01'
  , '1996-02-29'
  , '1996-02-07'
  , 3
  , 4.33
  , 'Laughing Bacchus Wine Cellars'
  , '2319 Elm St.'
  , 'Vancouver'
  , 'BC'
  , 'V3F 2K1'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10811
  , 'LINOD'
  , 8
  , '1996-02-02'
  , '1996-03-01'
  , '1996-02-08'
  , 1
  , 31.22
  , 'LINO-Delicateses'
  , 'Ave. 5 de Mayo Porlamar'
  , 'I. de Margarita'
  , 'Nueva Esparta'
  , '4980'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10812
  , 'REGGC'
  , 5
  , '1996-02-02'
  , '1996-03-01'
  , '1996-02-12'
  , 1
  , 59.78
  , 'Reggiani Caseifici'
  , 'Strada Provinciale 124'
  , 'Reggio Emilia'
  , NULL
  , '42100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10813
  , 'RICAR'
  , 1
  , '1996-02-05'
  , '1996-03-04'
  , '1996-02-09'
  , 1
  , 47.38
  , 'Ricardo Adocicados'
  , 'Av. Copacabana, 267'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-890'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10814
  , 'VICTE'
  , 3
  , '1996-02-05'
  , '1996-03-04'
  , '1996-02-14'
  , 3
  , 130.94
  , 'Victuailles en stock'
  , '2, rue du Commerce'
  , 'Lyon'
  , NULL
  , '69004'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10815
  , 'SAVEA'
  , 2
  , '1996-02-05'
  , '1996-03-04'
  , '1996-02-14'
  , 3
  , 14.62
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10816
  , 'GREAL'
  , 4
  , '1996-02-06'
  , '1996-03-05'
  , '1996-03-06'
  , 2
  , 719.78
  , 'Great Lakes Food Market'
  , '2732 Baker Blvd.'
  , 'Eugene'
  , 'OR'
  , '97403'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10817
  , 'KOENE'
  , 3
  , '1996-02-06'
  , '1996-02-20'
  , '1996-02-13'
  , 2
  , 306.07
  , 'Königlich Essen'
  , 'Maubelstr. 90'
  , 'Brandenburg'
  , NULL
  , '14776'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10818
  , 'MAGAA'
  , 7
  , '1996-02-07'
  , '1996-03-06'
  , '1996-02-12'
  , 3
  , 65.48
  , 'Magazzini Alimentari Riuniti'
  , 'Via Ludovico il Moro 22'
  , 'Bergamo'
  , NULL
  , '24100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10819
  , 'CACTU'
  , 2
  , '1996-02-07'
  , '1996-03-06'
  , '1996-02-16'
  , 3
  , 19.76
  , 'Cactus Comidas para llevar'
  , 'Cerrito 333'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10820
  , 'RATTC'
  , 3
  , '1996-02-07'
  , '1996-03-06'
  , '1996-02-13'
  , 2
  , 37.52
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10821
  , 'SPLIR'
  , 1
  , '1996-02-08'
  , '1996-03-07'
  , '1996-02-15'
  , 1
  , 36.68
  , 'Split Rail Beer & Ale'
  , 'P.O. Box 555'
  , 'Lander'
  , 'WY'
  , '82520'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10822
  , 'TRAIH'
  , 6
  , '1996-02-08'
  , '1996-03-07'
  , '1996-02-16'
  , 3
  , 7.00
  , 'Trail''s Head Gourmet Provisioners'
  , '722 DaVinci Blvd.'
  , 'Kirkland'
  , 'WA'
  , '98034'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10823
  , 'LILAS'
  , 5
  , '1996-02-09'
  , '1996-03-08'
  , '1996-02-13'
  , 2
  , 163.97
  , 'LILA-Supermercado'
  , 'Carrera 52 con Ave. Bolívar #65-98 Llano Largo'
  , 'Barquisimeto'
  , 'Lara'
  , '3508'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10824
  , 'FOLKO'
  , 8
  , '1996-02-09'
  , '1996-03-08'
  , '1996-03-01'
  , 1
  , 1.23
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10825
  , 'DRACD'
  , 1
  , '1996-02-09'
  , '1996-03-08'
  , '1996-02-14'
  , 1
  , 79.25
  , 'Drachenblut Delikatessen'
  , 'Walserweg 21'
  , 'Aachen'
  , NULL
  , '52066'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10826
  , 'BLONP'
  , 6
  , '1996-02-12'
  , '1996-03-11'
  , '1996-03-08'
  , 1
  , 7.09
  , 'Blondel père et fils'
  , '24, place Kléber'
  , 'Strasbourg'
  , NULL
  , '67000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10827
  , 'BONAP'
  , 1
  , '1996-02-12'
  , '1996-02-26'
  , '1996-03-08'
  , 2
  , 63.54
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10828
  , 'RANCH'
  , 9
  , '1996-02-13'
  , '1996-02-27'
  , '1996-03-06'
  , 1
  , 90.85
  , 'Rancho grande'
  , 'Av. del Libertador 900'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10829
  , 'ISLAT'
  , 9
  , '1996-02-13'
  , '1996-03-12'
  , '1996-02-23'
  , 1
  , 154.72
  , 'Island Trading'
  , 'Garden House
Crowther Way'
  , 'Cowes'
  , 'Isle of Wight'
  , 'PO31 7PJ'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10830
  , 'TRADH'
  , 4
  , '1996-02-13'
  , '1996-03-26'
  , '1996-02-21'
  , 2
  , 81.83
  , 'Tradição Hipermercados'
  , 'Av. Inês de Castro, 414'
  , 'São Paulo'
  , 'SP'
  , '05634-030'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10831
  , 'SANTG'
  , 3
  , '1996-02-14'
  , '1996-03-13'
  , '1996-02-23'
  , 2
  , 72.19
  , 'Sant?Gourmet'
  , 'Erling Skakkes gate 78'
  , 'Stavern'
  , NULL
  , '4110'
  , 'Norway'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10832
  , 'LAMAI'
  , 2
  , '1996-02-14'
  , '1996-03-13'
  , '1996-02-19'
  , 2
  , 43.26
  , 'La maison d''Asie'
  , '1 rue Alsace-Lorraine'
  , 'Toulouse'
  , NULL
  , '31000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10833
  , 'OTTIK'
  , 6
  , '1996-02-15'
  , '1996-03-14'
  , '1996-02-23'
  , 2
  , 71.49
  , 'Ottilies Käseladen'
  , 'Mehrheimerstr. 369'
  , 'Köln'
  , NULL
  , '50739'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10834
  , 'TRADH'
  , 1
  , '1996-02-15'
  , '1996-03-14'
  , '1996-02-19'
  , 3
  , 29.78
  , 'Tradição Hipermercados'
  , 'Av. Inês de Castro, 414'
  , 'São Paulo'
  , 'SP'
  , '05634-030'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10835
  , 'ALFKI'
  , 1
  , '1996-02-15'
  , '1996-03-14'
  , '1996-02-21'
  , 3
  , 69.53
  , 'Alfred''s Futterkiste'
  , 'Obere Str. 57'
  , 'Berlin'
  , NULL
  , '12209'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10836
  , 'ERNSH'
  , 7
  , '1996-02-16'
  , '1996-03-15'
  , '1996-02-21'
  , 1
  , 411.88
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10837
  , 'BERGS'
  , 9
  , '1996-02-16'
  , '1996-03-15'
  , '1996-02-23'
  , 3
  , 13.32
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10838
  , 'LINOD'
  , 3
  , '1996-02-19'
  , '1996-03-18'
  , '1996-02-23'
  , 3
  , 59.28
  , 'LINO-Delicateses'
  , 'Ave. 5 de Mayo Porlamar'
  , 'I. de Margarita'
  , 'Nueva Esparta'
  , '4980'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10839
  , 'TRADH'
  , 3
  , '1996-02-19'
  , '1996-03-18'
  , '1996-02-22'
  , 3
  , 35.43
  , 'Tradição Hipermercados'
  , 'Av. Inês de Castro, 414'
  , 'São Paulo'
  , 'SP'
  , '05634-030'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10840
  , 'LINOD'
  , 4
  , '1996-02-19'
  , '1996-04-01'
  , '1996-03-18'
  , 2
  , 2.71
  , 'LINO-Delicateses'
  , 'Ave. 5 de Mayo Porlamar'
  , 'I. de Margarita'
  , 'Nueva Esparta'
  , '4980'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10841
  , 'SUPRD'
  , 5
  , '1996-02-20'
  , '1996-03-19'
  , '1996-02-29'
  , 2
  , 424.30
  , 'Suprêmes délices'
  , 'Boulevard Tirou, 255'
  , 'Charleroi'
  , NULL
  , 'B-6000'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10842
  , 'TORTU'
  , 1
  , '1996-02-20'
  , '1996-03-19'
  , '1996-02-29'
  , 3
  , 54.42
  , 'Tortuga Restaurante'
  , 'Avda. Azteca 123'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10843
  , 'VICTE'
  , 4
  , '1996-02-21'
  , '1996-03-20'
  , '1996-02-26'
  , 2
  , 9.26
  , 'Victuailles en stock'
  , '2, rue du Commerce'
  , 'Lyon'
  , NULL
  , '69004'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10844
  , 'PICCO'
  , 8
  , '1996-02-21'
  , '1996-03-20'
  , '1996-02-26'
  , 2
  , 25.22
  , 'Piccolo und mehr'
  , 'Geislweg 14'
  , 'Salzburg'
  , NULL
  , '5020'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10845
  , 'QUICK'
  , 8
  , '1996-02-21'
  , '1996-03-06'
  , '1996-03-01'
  , 1
  , 212.98
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10846
  , 'SUPRD'
  , 2
  , '1996-02-22'
  , '1996-04-04'
  , '1996-02-23'
  , 3
  , 56.46
  , 'Suprêmes délices'
  , 'Boulevard Tirou, 255'
  , 'Charleroi'
  , NULL
  , 'B-6000'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10847
  , 'SAVEA'
  , 4
  , '1996-02-22'
  , '1996-03-07'
  , '1996-03-12'
  , 3
  , 487.57
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10848
  , 'CONSH'
  , 7
  , '1996-02-23'
  , '1996-03-22'
  , '1996-02-29'
  , 2
  , 38.24
  , 'Consolidated Holdings'
  , 'Berkeley Gardens
12  Brewery '
  , 'London'
  , NULL
  , 'WX1 6LT'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10849
  , 'KOENE'
  , 9
  , '1996-02-23'
  , '1996-03-22'
  , '1996-03-01'
  , 2
  , 0.56
  , 'Königlich Essen'
  , 'Maubelstr. 90'
  , 'Brandenburg'
  , NULL
  , '14776'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10850
  , 'VICTE'
  , 1
  , '1996-02-23'
  , '1996-04-05'
  , '1996-03-01'
  , 1
  , 49.19
  , 'Victuailles en stock'
  , '2, rue du Commerce'
  , 'Lyon'
  , NULL
  , '69004'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10851
  , 'RICAR'
  , 5
  , '1996-02-26'
  , '1996-03-25'
  , '1996-03-04'
  , 1
  , 160.55
  , 'Ricardo Adocicados'
  , 'Av. Copacabana, 267'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-890'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10852
  , 'RATTC'
  , 8
  , '1996-02-26'
  , '1996-03-11'
  , '1996-03-01'
  , 1
  , 174.05
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10853
  , 'BLAUS'
  , 9
  , '1996-02-27'
  , '1996-03-26'
  , '1996-03-05'
  , 2
  , 53.83
  , 'Blauer See Delikatessen'
  , 'Forsterstr. 57'
  , 'Mannheim'
  , NULL
  , '68306'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10854
  , 'ERNSH'
  , 3
  , '1996-02-27'
  , '1996-03-26'
  , '1996-03-07'
  , 2
  , 100.22
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10855
  , 'OLDWO'
  , 3
  , '1996-02-27'
  , '1996-03-26'
  , '1996-03-06'
  , 1
  , 170.97
  , 'Old World Delicatessen'
  , '2743 Bering St.'
  , 'Anchorage'
  , 'AK'
  , '99508'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10856
  , 'ANTON'
  , 3
  , '1996-02-28'
  , '1996-03-27'
  , '1996-03-12'
  , 2
  , 58.43
  , 'Antonio Moreno Taquería'
  , 'Mataderos  2312'
  , 'México D.F.'
  , NULL
  , '05023'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10857
  , 'BERGS'
  , 8
  , '1996-02-28'
  , '1996-03-27'
  , '1996-03-08'
  , 2
  , 188.85
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10858
  , 'LACOR'
  , 2
  , '1996-02-29'
  , '1996-03-28'
  , '1996-03-05'
  , 1
  , 52.51
  , 'La corne d''abondance'
  , '67, avenue de l''Europe'
  , 'Versailles'
  , NULL
  , '78000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10859
  , 'FRANK'
  , 1
  , '1996-02-29'
  , '1996-03-28'
  , '1996-03-04'
  , 2
  , 76.10
  , 'Frankenversand'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10860
  , 'FRANR'
  , 3
  , '1996-02-29'
  , '1996-03-28'
  , '1996-03-06'
  , 3
  , 19.26
  , 'France restauration'
  , '54, rue Royale'
  , 'Nantes'
  , NULL
  , '44000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10861
  , 'WHITC'
  , 4
  , '1996-03-01'
  , '1996-03-29'
  , '1996-03-19'
  , 2
  , 14.93
  , 'White Clover Markets'
  , '1029 - 12th Ave. S.'
  , 'Seattle'
  , 'WA'
  , '98124'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10862
  , 'LEHMS'
  , 8
  , '1996-03-01'
  , '1996-04-12'
  , '1996-03-04'
  , 2
  , 53.23
  , 'Lehmanns Marktstand'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10863
  , 'HILAA'
  , 4
  , '1996-03-04'
  , '1996-04-01'
  , '1996-03-19'
  , 2
  , 30.26
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10864
  , 'AROUT'
  , 4
  , '1996-03-04'
  , '1996-04-01'
  , '1996-03-11'
  , 2
  , 3.04
  , 'Around the Horn'
  , 'Brook Farm
Stratford St. Mary'
  , 'Colchester'
  , 'Essex'
  , 'CO7 6JX'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10865
  , 'QUICK'
  , 2
  , '1996-03-04'
  , '1996-03-18'
  , '1996-03-14'
  , 1
  , 348.14
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10866
  , 'BERGS'
  , 5
  , '1996-03-05'
  , '1996-04-02'
  , '1996-03-14'
  , 1
  , 109.11
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10867
  , 'LONEP'
  , 6
  , '1996-03-05'
  , '1996-04-16'
  , '1996-03-13'
  , 1
  , 1.93
  , 'Lonesome Pine Restaurant'
  , '89 Chiaroscuro Rd.'
  , 'Portland'
  , 'OR'
  , '97219'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10868
  , 'QUEEN'
  , 7
  , '1996-03-06'
  , '1996-04-03'
  , '1996-03-25'
  , 2
  , 191.27
  , 'Queen Cozinha'
  , 'Alameda dos Canàrios, 891'
  , 'São Paulo'
  , 'SP'
  , '05487-020'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10869
  , 'SEVES'
  , 5
  , '1996-03-06'
  , '1996-04-03'
  , '1996-03-11'
  , 1
  , 143.28
  , 'Seven Seas Imports'
  , '90 Wadhurst Rd.'
  , 'London'
  , NULL
  , 'OX15 4NB'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10870
  , 'WOLZA'
  , 5
  , '1996-03-06'
  , '1996-04-03'
  , '1996-03-15'
  , 3
  , 12.04
  , 'Wolski Zajazd'
  , 'ul. Filtrowa 68'
  , 'Warszawa'
  , NULL
  , '01-012'
  , 'Poland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10871
  , 'BONAP'
  , 9
  , '1996-03-07'
  , '1996-04-04'
  , '1996-03-12'
  , 2
  , 112.27
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10872
  , 'GODOS'
  , 5
  , '1996-03-07'
  , '1996-04-04'
  , '1996-03-11'
  , 2
  , 175.32
  , 'Godos Cocina Típica'
  , 'C/ Romero, 33'
  , 'Sevilla'
  , NULL
  , '41101'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10873
  , 'WILMK'
  , 4
  , '1996-03-08'
  , '1996-04-05'
  , '1996-03-11'
  , 1
  , 0.82
  , 'Wilman Kala'
  , 'Keskuskatu 45'
  , 'Helsinki'
  , NULL
  , '21240'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10874
  , 'GODOS'
  , 5
  , '1996-03-08'
  , '1996-04-05'
  , '1996-03-13'
  , 2
  , 19.58
  , 'Godos Cocina Típica'
  , 'C/ Romero, 33'
  , 'Sevilla'
  , NULL
  , '41101'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10875
  , 'BERGS'
  , 4
  , '1996-03-08'
  , '1996-04-05'
  , '1996-04-02'
  , 2
  , 32.37
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10876
  , 'BONAP'
  , 7
  , '1996-03-11'
  , '1996-04-08'
  , '1996-03-14'
  , 3
  , 60.42
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10877
  , 'RICAR'
  , 1
  , '1996-03-11'
  , '1996-04-08'
  , '1996-03-21'
  , 1
  , 38.06
  , 'Ricardo Adocicados'
  , 'Av. Copacabana, 267'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-890'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10878
  , 'QUICK'
  , 4
  , '1996-03-12'
  , '1996-04-09'
  , '1996-03-14'
  , 1
  , 46.69
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10879
  , 'WILMK'
  , 3
  , '1996-03-12'
  , '1996-04-09'
  , '1996-03-14'
  , 3
  , 8.50
  , 'Wilman Kala'
  , 'Keskuskatu 45'
  , 'Helsinki'
  , NULL
  , '21240'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10880
  , 'FOLKO'
  , 7
  , '1996-03-12'
  , '1996-04-23'
  , '1996-03-20'
  , 1
  , 88.01
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10881
  , 'CACTU'
  , 4
  , '1996-03-13'
  , '1996-04-10'
  , '1996-03-20'
  , 1
  , 2.84
  , 'Cactus Comidas para llevar'
  , 'Cerrito 333'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10882
  , 'SAVEA'
  , 4
  , '1996-03-13'
  , '1996-04-10'
  , '1996-03-22'
  , 3
  , 23.10
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10883
  , 'LONEP'
  , 8
  , '1996-03-14'
  , '1996-04-11'
  , '1996-03-22'
  , 3
  , 0.53
  , 'Lonesome Pine Restaurant'
  , '89 Chiaroscuro Rd.'
  , 'Portland'
  , 'OR'
  , '97219'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10884
  , 'LETSS'
  , 4
  , '1996-03-14'
  , '1996-04-11'
  , '1996-03-15'
  , 2
  , 90.97
  , 'Let''s Stop N Shop'
  , '87 Polk St.
Suite 5'
  , 'San Francisco'
  , 'CA'
  , '94117'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10885
  , 'SUPRD'
  , 6
  , '1996-03-14'
  , '1996-04-11'
  , '1996-03-20'
  , 3
  , 5.64
  , 'Suprêmes délices'
  , 'Boulevard Tirou, 255'
  , 'Charleroi'
  , NULL
  , 'B-6000'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10886
  , 'HANAR'
  , 1
  , '1996-03-15'
  , '1996-04-12'
  , '1996-04-01'
  , 1
  , 4.99
  , 'Hanari Carnes'
  , 'Rua do Paço, 67'
  , 'Rio de Janeiro'
  , 'RJ'
  , '05454-876'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10887
  , 'GALED'
  , 8
  , '1996-03-15'
  , '1996-04-12'
  , '1996-03-18'
  , 3
  , 1.25
  , 'Galería del gastronómo'
  , 'Rambla de Cataluña, 23'
  , 'Barcelona'
  , NULL
  , '8022'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10888
  , 'GODOS'
  , 1
  , '1996-03-18'
  , '1996-04-15'
  , '1996-03-25'
  , 2
  , 51.87
  , 'Godos Cocina Típica'
  , 'C/ Romero, 33'
  , 'Sevilla'
  , NULL
  , '41101'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10889
  , 'RATTC'
  , 9
  , '1996-03-18'
  , '1996-04-15'
  , '1996-03-25'
  , 3
  , 280.61
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10890
  , 'DUMON'
  , 7
  , '1996-03-18'
  , '1996-04-15'
  , '1996-03-20'
  , 1
  , 32.76
  , 'Du monde entier'
  , '67, rue des Cinquante Otages'
  , 'Nantes'
  , NULL
  , '44000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10891
  , 'LEHMS'
  , 7
  , '1996-03-19'
  , '1996-04-16'
  , '1996-03-21'
  , 2
  , 20.37
  , 'Lehmanns Marktstand'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10892
  , 'MAISD'
  , 4
  , '1996-03-19'
  , '1996-04-16'
  , '1996-03-21'
  , 2
  , 120.27
  , 'Maison Dewey'
  , 'Rue Joseph-Bens 532'
  , 'Bruxelles'
  , NULL
  , 'B-1180'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10893
  , 'KOENE'
  , 9
  , '1996-03-20'
  , '1996-04-17'
  , '1996-03-22'
  , 2
  , 77.78
  , 'Königlich Essen'
  , 'Maubelstr. 90'
  , 'Brandenburg'
  , NULL
  , '14776'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10894
  , 'SAVEA'
  , 1
  , '1996-03-20'
  , '1996-04-17'
  , '1996-03-22'
  , 1
  , 116.13
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10895
  , 'ERNSH'
  , 3
  , '1996-03-20'
  , '1996-04-17'
  , '1996-03-25'
  , 1
  , 162.75
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10896
  , 'MAISD'
  , 7
  , '1996-03-21'
  , '1996-04-18'
  , '1996-03-29'
  , 3
  , 32.45
  , 'Maison Dewey'
  , 'Rue Joseph-Bens 532'
  , 'Bruxelles'
  , NULL
  , 'B-1180'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10897
  , 'HUNGO'
  , 3
  , '1996-03-21'
  , '1996-04-18'
  , '1996-03-27'
  , 2
  , 603.54
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10898
  , 'OCEAN'
  , 4
  , '1996-03-22'
  , '1996-04-19'
  , '1996-04-05'
  , 2
  , 1.27
  , 'Océano Atlántico Ltda.'
  , 'Ing. Gustavo Moncada 8585
Piso 20-A'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10899
  , 'LILAS'
  , 5
  , '1996-03-22'
  , '1996-04-19'
  , '1996-03-28'
  , 3
  , 1.21
  , 'LILA-Supermercado'
  , 'Carrera 52 con Ave. Bolívar #65-98 Llano Largo'
  , 'Barquisimeto'
  , 'Lara'
  , '3508'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10900
  , 'WELLI'
  , 1
  , '1996-03-22'
  , '1996-04-19'
  , '1996-04-03'
  , 2
  , 1.66
  , 'Wellington Importadora'
  , 'Rua do Mercado, 12'
  , 'Resende'
  , 'SP'
  , '08737-363'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10901
  , 'HILAA'
  , 4
  , '1996-03-25'
  , '1996-04-22'
  , '1996-03-28'
  , 1
  , 62.09
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10902
  , 'FOLKO'
  , 1
  , '1996-03-25'
  , '1996-04-22'
  , '1996-04-02'
  , 1
  , 44.15
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10903
  , 'HANAR'
  , 3
  , '1996-03-26'
  , '1996-04-23'
  , '1996-04-03'
  , 3
  , 36.71
  , 'Hanari Carnes'
  , 'Rua do Paço, 67'
  , 'Rio de Janeiro'
  , 'RJ'
  , '05454-876'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10904
  , 'WHITC'
  , 3
  , '1996-03-26'
  , '1996-04-23'
  , '1996-03-29'
  , 3
  , 162.95
  , 'White Clover Markets'
  , '1029 - 12th Ave. S.'
  , 'Seattle'
  , 'WA'
  , '98124'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10905
  , 'WELLI'
  , 9
  , '1996-03-26'
  , '1996-04-23'
  , '1996-04-05'
  , 2
  , 13.72
  , 'Wellington Importadora'
  , 'Rua do Mercado, 12'
  , 'Resende'
  , 'SP'
  , '08737-363'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10906
  , 'WOLZA'
  , 4
  , '1996-03-27'
  , '1996-04-10'
  , '1996-04-02'
  , 3
  , 26.29
  , 'Wolski Zajazd'
  , 'ul. Filtrowa 68'
  , 'Warszawa'
  , NULL
  , '01-012'
  , 'Poland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10907
  , 'SPECD'
  , 6
  , '1996-03-27'
  , '1996-04-24'
  , '1996-03-29'
  , 3
  , 9.19
  , 'Spécialités du monde'
  , '25, rue Lauriston'
  , 'Paris'
  , NULL
  , '75016'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10908
  , 'REGGC'
  , 4
  , '1996-03-28'
  , '1996-04-25'
  , '1996-04-05'
  , 2
  , 32.96
  , 'Reggiani Caseifici'
  , 'Strada Provinciale 124'
  , 'Reggio Emilia'
  , NULL
  , '42100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10909
  , 'SANTG'
  , 1
  , '1996-03-28'
  , '1996-04-25'
  , '1996-04-09'
  , 2
  , 53.05
  , 'Sant?Gourmet'
  , 'Erling Skakkes gate 78'
  , 'Stavern'
  , NULL
  , '4110'
  , 'Norway'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10910
  , 'WILMK'
  , 1
  , '1996-03-28'
  , '1996-04-25'
  , '1996-04-03'
  , 3
  , 38.11
  , 'Wilman Kala'
  , 'Keskuskatu 45'
  , 'Helsinki'
  , NULL
  , '21240'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10911
  , 'GODOS'
  , 3
  , '1996-03-28'
  , '1996-04-25'
  , '1996-04-04'
  , 1
  , 38.19
  , 'Godos Cocina Típica'
  , 'C/ Romero, 33'
  , 'Sevilla'
  , NULL
  , '41101'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10912
  , 'HUNGO'
  , 2
  , '1996-03-28'
  , '1996-04-25'
  , '1996-04-17'
  , 2
  , 580.91
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10913
  , 'QUEEN'
  , 4
  , '1996-03-28'
  , '1996-04-25'
  , '1996-04-03'
  , 1
  , 33.05
  , 'Queen Cozinha'
  , 'Alameda dos Canàrios, 891'
  , 'São Paulo'
  , 'SP'
  , '05487-020'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10914
  , 'QUEEN'
  , 6
  , '1996-03-29'
  , '1996-04-26'
  , '1996-04-01'
  , 1
  , 21.19
  , 'Queen Cozinha'
  , 'Alameda dos Canàrios, 891'
  , 'São Paulo'
  , 'SP'
  , '05487-020'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10915
  , 'TORTU'
  , 2
  , '1996-03-29'
  , '1996-04-26'
  , '1996-04-01'
  , 2
  , 3.51
  , 'Tortuga Restaurante'
  , 'Avda. Azteca 123'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10916
  , 'RANCH'
  , 1
  , '1996-03-29'
  , '1996-04-26'
  , '1996-04-08'
  , 2
  , 63.77
  , 'Rancho grande'
  , 'Av. del Libertador 900'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10917
  , 'ROMEY'
  , 4
  , '1996-04-01'
  , '1996-04-29'
  , '1996-04-10'
  , 2
  , 8.29
  , 'Romero y tomillo'
  , 'Gran Vía, 1'
  , 'Madrid'
  , NULL
  , '28001'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10918
  , 'BOTTM'
  , 3
  , '1996-04-01'
  , '1996-04-29'
  , '1996-04-10'
  , 3
  , 48.83
  , 'Bottom-Dollar Markets'
  , '23 Tsawassen Blvd.'
  , 'Tsawassen'
  , 'BC'
  , 'T2F 8M4'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10919
  , 'LINOD'
  , 2
  , '1996-04-01'
  , '1996-04-29'
  , '1996-04-03'
  , 2
  , 19.80
  , 'LINO-Delicateses'
  , 'Ave. 5 de Mayo Porlamar'
  , 'I. de Margarita'
  , 'Nueva Esparta'
  , '4980'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10920
  , 'AROUT'
  , 4
  , '1996-04-02'
  , '1996-04-30'
  , '1996-04-08'
  , 2
  , 29.61
  , 'Around the Horn'
  , 'Brook Farm
Stratford St. Mary'
  , 'Colchester'
  , 'Essex'
  , 'CO7 6JX'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10921
  , 'VAFFE'
  , 1
  , '1996-04-02'
  , '1996-05-14'
  , '1996-04-08'
  , 1
  , 176.48
  , 'Vaffeljernet'
  , 'Smagsløget 45'
  , 'Århus'
  , NULL
  , '8200'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10922
  , 'HANAR'
  , 5
  , '1996-04-02'
  , '1996-04-30'
  , '1996-04-04'
  , 3
  , 62.74
  , 'Hanari Carnes'
  , 'Rua do Paço, 67'
  , 'Rio de Janeiro'
  , 'RJ'
  , '05454-876'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10923
  , 'LAMAI'
  , 7
  , '1996-04-02'
  , '1996-05-14'
  , '1996-04-12'
  , 3
  , 68.26
  , 'La maison d''Asie'
  , '1 rue Alsace-Lorraine'
  , 'Toulouse'
  , NULL
  , '31000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10924
  , 'BERGS'
  , 3
  , '1996-04-03'
  , '1996-05-01'
  , '1996-05-08'
  , 2
  , 151.52
  , 'Berglunds snabbköp'
  , 'Berguvsvägen  8'
  , 'Lule?'
  , NULL
  , 'S-958 22'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10925
  , 'HANAR'
  , 3
  , '1996-04-03'
  , '1996-05-01'
  , '1996-04-12'
  , 1
  , 2.27
  , 'Hanari Carnes'
  , 'Rua do Paço, 67'
  , 'Rio de Janeiro'
  , 'RJ'
  , '05454-876'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10926
  , 'ANATR'
  , 4
  , '1996-04-03'
  , '1996-05-01'
  , '1996-04-10'
  , 3
  , 39.92
  , 'Ana Trujillo Emparedados y helados'
  , 'Avda. de la Constitución 2222'
  , 'México D.F.'
  , NULL
  , '05021'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10927
  , 'LACOR'
  , 4
  , '1996-04-04'
  , '1996-05-02'
  , '1996-05-08'
  , 1
  , 19.79
  , 'La corne d''abondance'
  , '67, avenue de l''Europe'
  , 'Versailles'
  , NULL
  , '78000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10928
  , 'GALED'
  , 1
  , '1996-04-04'
  , '1996-05-02'
  , '1996-04-17'
  , 1
  , 1.36
  , 'Galería del gastronómo'
  , 'Rambla de Cataluña, 23'
  , 'Barcelona'
  , NULL
  , '8022'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10929
  , 'FRANK'
  , 6
  , '1996-04-04'
  , '1996-05-02'
  , '1996-04-11'
  , 1
  , 33.93
  , 'Frankenversand'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10930
  , 'SUPRD'
  , 4
  , '1996-04-05'
  , '1996-05-17'
  , '1996-04-17'
  , 3
  , 15.55
  , 'Suprêmes délices'
  , 'Boulevard Tirou, 255'
  , 'Charleroi'
  , NULL
  , 'B-6000'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10931
  , 'RICSU'
  , 4
  , '1996-04-05'
  , '1996-04-19'
  , '1996-04-18'
  , 2
  , 13.60
  , 'Richter Supermarkt'
  , 'Starenweg 5'
  , 'Genève'
  , NULL
  , '1204'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10932
  , 'BONAP'
  , 8
  , '1996-04-05'
  , '1996-05-03'
  , '1996-04-23'
  , 1
  , 134.64
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10933
  , 'ISLAT'
  , 6
  , '1996-04-05'
  , '1996-05-03'
  , '1996-04-15'
  , 3
  , 54.15
  , 'Island Trading'
  , 'Garden House
Crowther Way'
  , 'Cowes'
  , 'Isle of Wight'
  , 'PO31 7PJ'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10934
  , 'LEHMS'
  , 3
  , '1996-04-08'
  , '1996-05-06'
  , '1996-04-11'
  , 3
  , 32.01
  , 'Lehmanns Marktstand'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10935
  , 'WELLI'
  , 4
  , '1996-04-08'
  , '1996-05-06'
  , '1996-04-17'
  , 3
  , 47.59
  , 'Wellington Importadora'
  , 'Rua do Mercado, 12'
  , 'Resende'
  , 'SP'
  , '08737-363'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10936
  , 'GREAL'
  , 3
  , '1996-04-08'
  , '1996-05-06'
  , '1996-04-17'
  , 2
  , 33.68
  , 'Great Lakes Food Market'
  , '2732 Baker Blvd.'
  , 'Eugene'
  , 'OR'
  , '97403'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10937
  , 'CACTU'
  , 7
  , '1996-04-09'
  , '1996-04-23'
  , '1996-04-12'
  , 3
  , 31.51
  , 'Cactus Comidas para llevar'
  , 'Cerrito 333'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10938
  , 'QUICK'
  , 3
  , '1996-04-09'
  , '1996-05-07'
  , '1996-04-15'
  , 2
  , 31.89
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10939
  , 'MAGAA'
  , 2
  , '1996-04-09'
  , '1996-05-07'
  , '1996-04-12'
  , 2
  , 76.33
  , 'Magazzini Alimentari Riuniti'
  , 'Via Ludovico il Moro 22'
  , 'Bergamo'
  , NULL
  , '24100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10940
  , 'BONAP'
  , 8
  , '1996-04-10'
  , '1996-05-08'
  , '1996-04-22'
  , 3
  , 19.77
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10941
  , 'SAVEA'
  , 7
  , '1996-04-10'
  , '1996-05-08'
  , '1996-04-19'
  , 2
  , 400.81
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10942
  , 'REGGC'
  , 9
  , '1996-04-10'
  , '1996-05-08'
  , '1996-04-17'
  , 3
  , 17.95
  , 'Reggiani Caseifici'
  , 'Strada Provinciale 124'
  , 'Reggio Emilia'
  , NULL
  , '42100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10943
  , 'BSBEV'
  , 4
  , '1996-04-10'
  , '1996-05-08'
  , '1996-04-18'
  , 2
  , 2.17
  , 'B''s Beverages'
  , 'Fauntleroy Circus'
  , 'London'
  , NULL
  , 'EC2 5NT'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10944
  , 'BOTTM'
  , 6
  , '1996-04-11'
  , '1996-04-25'
  , '1996-04-12'
  , 3
  , 52.92
  , 'Bottom-Dollar Markets'
  , '23 Tsawassen Blvd.'
  , 'Tsawassen'
  , 'BC'
  , 'T2F 8M4'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10945
  , 'MORGK'
  , 4
  , '1996-04-11'
  , '1996-05-09'
  , '1996-04-17'
  , 1
  , 10.22
  , 'Morgenstern Gesundkost'
  , 'Heerstr. 22'
  , 'Leipzig'
  , NULL
  , '04179'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10946
  , 'VAFFE'
  , 1
  , '1996-04-11'
  , '1996-05-09'
  , '1996-04-18'
  , 2
  , 27.20
  , 'Vaffeljernet'
  , 'Smagsløget 45'
  , 'Århus'
  , NULL
  , '8200'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10947
  , 'BSBEV'
  , 3
  , '1996-04-12'
  , '1996-05-10'
  , '1996-04-15'
  , 2
  , 3.26
  , 'B''s Beverages'
  , 'Fauntleroy Circus'
  , 'London'
  , NULL
  , 'EC2 5NT'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10948
  , 'GODOS'
  , 3
  , '1996-04-12'
  , '1996-05-10'
  , '1996-04-18'
  , 3
  , 23.39
  , 'Godos Cocina Típica'
  , 'C/ Romero, 33'
  , 'Sevilla'
  , NULL
  , '41101'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10949
  , 'BOTTM'
  , 2
  , '1996-04-12'
  , '1996-05-10'
  , '1996-04-16'
  , 3
  , 74.44
  , 'Bottom-Dollar Markets'
  , '23 Tsawassen Blvd.'
  , 'Tsawassen'
  , 'BC'
  , 'T2F 8M4'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10950
  , 'MAGAA'
  , 1
  , '1996-04-15'
  , '1996-05-13'
  , '1996-04-22'
  , 2
  , 2.50
  , 'Magazzini Alimentari Riuniti'
  , 'Via Ludovico il Moro 22'
  , 'Bergamo'
  , NULL
  , '24100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10951
  , 'RICSU'
  , 9
  , '1996-04-15'
  , '1996-05-27'
  , '1996-05-07'
  , 2
  , 30.85
  , 'Richter Supermarkt'
  , 'Starenweg 5'
  , 'Genève'
  , NULL
  , '1204'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10952
  , 'ALFKI'
  , 1
  , '1996-04-15'
  , '1996-05-27'
  , '1996-04-23'
  , 1
  , 40.42
  , 'Alfred''s Futterkiste'
  , 'Obere Str. 57'
  , 'Berlin'
  , NULL
  , '12209'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10953
  , 'AROUT'
  , 9
  , '1996-04-15'
  , '1996-04-29'
  , '1996-04-24'
  , 2
  , 23.72
  , 'Around the Horn'
  , 'Brook Farm
Stratford St. Mary'
  , 'Colchester'
  , 'Essex'
  , 'CO7 6JX'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10954
  , 'LINOD'
  , 5
  , '1996-04-16'
  , '1996-05-28'
  , '1996-04-19'
  , 1
  , 27.91
  , 'LINO-Delicateses'
  , 'Ave. 5 de Mayo Porlamar'
  , 'I. de Margarita'
  , 'Nueva Esparta'
  , '4980'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10955
  , 'FOLKO'
  , 8
  , '1996-04-16'
  , '1996-05-14'
  , '1996-04-19'
  , 2
  , 3.26
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10956
  , 'BLAUS'
  , 6
  , '1996-04-16'
  , '1996-05-28'
  , '1996-04-19'
  , 2
  , 44.65
  , 'Blauer See Delikatessen'
  , 'Forsterstr. 57'
  , 'Mannheim'
  , NULL
  , '68306'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10957
  , 'HILAA'
  , 8
  , '1996-04-17'
  , '1996-05-15'
  , '1996-04-26'
  , 3
  , 105.36
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10958
  , 'OCEAN'
  , 7
  , '1996-04-17'
  , '1996-05-15'
  , '1996-04-26'
  , 2
  , 49.56
  , 'Océano Atlántico Ltda.'
  , 'Ing. Gustavo Moncada 8585
Piso 20-A'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10959
  , 'GOURL'
  , 6
  , '1996-04-17'
  , '1996-05-29'
  , '1996-04-22'
  , 2
  , 4.98
  , 'Gourmet Lanchonetes'
  , 'Av. Brasil, 442'
  , 'Campinas'
  , 'SP'
  , '04876-786'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10960
  , 'HILAA'
  , 3
  , '1996-04-18'
  , '1996-05-02'
  , '1996-05-08'
  , 1
  , 2.08
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10961
  , 'QUEEN'
  , 8
  , '1996-04-18'
  , '1996-05-16'
  , '1996-04-29'
  , 1
  , 104.47
  , 'Queen Cozinha'
  , 'Alameda dos Canàrios, 891'
  , 'São Paulo'
  , 'SP'
  , '05487-020'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10962
  , 'QUICK'
  , 8
  , '1996-04-18'
  , '1996-05-16'
  , '1996-04-22'
  , 2
  , 275.79
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10963
  , 'FURIB'
  , 9
  , '1996-04-18'
  , '1996-05-16'
  , '1996-04-25'
  , 3
  , 2.70
  , 'Furia Bacalhau e Frutos do Mar'
  , 'Jardim das rosas n. 32'
  , 'Lisboa'
  , NULL
  , '1675'
  , 'Portugal'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10964
  , 'SPECD'
  , 3
  , '1996-04-19'
  , '1996-05-17'
  , '1996-04-23'
  , 2
  , 87.38
  , 'Spécialités du monde'
  , '25, rue Lauriston'
  , 'Paris'
  , NULL
  , '75016'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10965
  , 'OLDWO'
  , 6
  , '1996-04-19'
  , '1996-05-17'
  , '1996-04-29'
  , 3
  , 144.38
  , 'Old World Delicatessen'
  , '2743 Bering St.'
  , 'Anchorage'
  , 'AK'
  , '99508'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10966
  , 'CHOPS'
  , 4
  , '1996-04-19'
  , '1996-05-17'
  , '1996-05-08'
  , 1
  , 27.19
  , 'Chop-suey Chinese'
  , 'Hauptstr. 31'
  , 'Bern'
  , NULL
  , '3012'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10967
  , 'TOMSP'
  , 2
  , '1996-04-22'
  , '1996-05-20'
  , '1996-05-02'
  , 2
  , 62.22
  , 'Toms Spezialitäten'
  , 'Luisenstr. 48'
  , 'Münster'
  , NULL
  , '44087'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10968
  , 'ERNSH'
  , 1
  , '1996-04-22'
  , '1996-05-20'
  , '1996-05-01'
  , 3
  , 74.60
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10969
  , 'COMMI'
  , 1
  , '1996-04-22'
  , '1996-05-20'
  , '1996-04-29'
  , 2
  , 0.21
  , 'Comércio Mineiro'
  , 'Av. dos Lusíadas, 23'
  , 'São Paulo'
  , 'SP'
  , '05432-043'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10970
  , 'BOLID'
  , 9
  , '1996-04-23'
  , '1996-05-07'
  , '1996-05-24'
  , 1
  , 16.16
  , 'Bólido Comidas preparadas'
  , 'C/ Araquil, 67'
  , 'Madrid'
  , NULL
  , '28023'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10971
  , 'FRANR'
  , 2
  , '1996-04-23'
  , '1996-05-21'
  , '1996-05-02'
  , 2
  , 121.82
  , 'France restauration'
  , '54, rue Royale'
  , 'Nantes'
  , NULL
  , '44000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10972
  , 'LACOR'
  , 4
  , '1996-04-23'
  , '1996-05-21'
  , '1996-04-25'
  , 2
  , 0.02
  , 'La corne d''abondance'
  , '67, avenue de l''Europe'
  , 'Versailles'
  , NULL
  , '78000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10973
  , 'LACOR'
  , 6
  , '1996-04-23'
  , '1996-05-21'
  , '1996-04-26'
  , 2
  , 15.17
  , 'La corne d''abondance'
  , '67, avenue de l''Europe'
  , 'Versailles'
  , NULL
  , '78000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10974
  , 'SPLIR'
  , 3
  , '1996-04-24'
  , '1996-05-08'
  , '1996-05-03'
  , 3
  , 12.96
  , 'Split Rail Beer & Ale'
  , 'P.O. Box 555'
  , 'Lander'
  , 'WY'
  , '82520'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10975
  , 'BOTTM'
  , 1
  , '1996-04-24'
  , '1996-05-22'
  , '1996-04-26'
  , 3
  , 32.27
  , 'Bottom-Dollar Markets'
  , '23 Tsawassen Blvd.'
  , 'Tsawassen'
  , 'BC'
  , 'T2F 8M4'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10976
  , 'HILAA'
  , 1
  , '1996-04-24'
  , '1996-06-05'
  , '1996-05-03'
  , 1
  , 37.97
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10977
  , 'FOLKO'
  , 8
  , '1996-04-25'
  , '1996-05-23'
  , '1996-05-10'
  , 3
  , 208.50
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10978
  , 'MAISD'
  , 9
  , '1996-04-25'
  , '1996-05-23'
  , '1996-05-23'
  , 2
  , 32.82
  , 'Maison Dewey'
  , 'Rue Joseph-Bens 532'
  , 'Bruxelles'
  , NULL
  , 'B-1180'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10979
  , 'ERNSH'
  , 8
  , '1996-04-25'
  , '1996-05-23'
  , '1996-04-30'
  , 2
  , 353.07
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10980
  , 'FOLKO'
  , 4
  , '1996-04-26'
  , '1996-06-07'
  , '1996-05-17'
  , 1
  , 1.26
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10981
  , 'HANAR'
  , 1
  , '1996-04-26'
  , '1996-05-24'
  , '1996-05-02'
  , 2
  , 193.37
  , 'Hanari Carnes'
  , 'Rua do Paço, 67'
  , 'Rio de Janeiro'
  , 'RJ'
  , '05454-876'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10982
  , 'BOTTM'
  , 2
  , '1996-04-26'
  , '1996-05-24'
  , '1996-05-08'
  , 1
  , 14.01
  , 'Bottom-Dollar Markets'
  , '23 Tsawassen Blvd.'
  , 'Tsawassen'
  , 'BC'
  , 'T2F 8M4'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10983
  , 'SAVEA'
  , 2
  , '1996-04-26'
  , '1996-05-24'
  , '1996-05-06'
  , 2
  , 657.54
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10984
  , 'SAVEA'
  , 1
  , '1996-04-29'
  , '1996-05-27'
  , '1996-05-03'
  , 3
  , 211.22
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10985
  , 'HUNGO'
  , 2
  , '1996-04-29'
  , '1996-05-27'
  , '1996-05-02'
  , 1
  , 91.51
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10986
  , 'OCEAN'
  , 8
  , '1996-04-29'
  , '1996-05-27'
  , '1996-05-21'
  , 2
  , 217.86
  , 'Océano Atlántico Ltda.'
  , 'Ing. Gustavo Moncada 8585
Piso 20-A'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10987
  , 'EASTC'
  , 8
  , '1996-04-30'
  , '1996-05-28'
  , '1996-05-06'
  , 1
  , 185.48
  , 'Eastern Connection'
  , '35 King George'
  , 'London'
  , NULL
  , 'WX3 6FW'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10988
  , 'RATTC'
  , 3
  , '1996-04-30'
  , '1996-05-28'
  , '1996-05-10'
  , 2
  , 61.14
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10989
  , 'QUEDE'
  , 2
  , '1996-04-30'
  , '1996-05-28'
  , '1996-05-02'
  , 1
  , 34.76
  , 'Que Delícia'
  , 'Rua da Panificadora, 12'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-673'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10990
  , 'ERNSH'
  , 2
  , '1996-05-01'
  , '1996-06-12'
  , '1996-05-07'
  , 3
  , 117.61
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10991
  , 'QUICK'
  , 1
  , '1996-05-01'
  , '1996-05-29'
  , '1996-05-07'
  , 1
  , 38.51
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10992
  , 'THEBI'
  , 1
  , '1996-05-01'
  , '1996-05-29'
  , '1996-05-03'
  , 3
  , 4.27
  , 'The Big Cheese'
  , '89 Jefferson Way
Suite 2'
  , 'Portland'
  , 'OR'
  , '97201'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10993
  , 'FOLKO'
  , 7
  , '1996-05-01'
  , '1996-05-29'
  , '1996-05-10'
  , 3
  , 8.81
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10994
  , 'VAFFE'
  , 2
  , '1996-05-02'
  , '1996-05-16'
  , '1996-05-09'
  , 3
  , 65.53
  , 'Vaffeljernet'
  , 'Smagsløget 45'
  , 'Århus'
  , NULL
  , '8200'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10995
  , 'PERIC'
  , 1
  , '1996-05-02'
  , '1996-05-30'
  , '1996-05-06'
  , 3
  , 46.00
  , 'Pericles Comidas clásicas'
  , 'Calle Dr. Jorge Cash 321'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10996
  , 'QUICK'
  , 4
  , '1996-05-02'
  , '1996-05-30'
  , '1996-05-10'
  , 2
  , 1.12
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10997
  , 'LILAS'
  , 8
  , '1996-05-03'
  , '1996-06-14'
  , '1996-05-13'
  , 2
  , 73.91
  , 'LILA-Supermercado'
  , 'Carrera 52 con Ave. Bolívar #65-98 Llano Largo'
  , 'Barquisimeto'
  , 'Lara'
  , '3508'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10998
  , 'WOLZA'
  , 8
  , '1996-05-03'
  , '1996-05-17'
  , '1996-05-17'
  , 2
  , 20.31
  , 'Wolski Zajazd'
  , 'ul. Filtrowa 68'
  , 'Warszawa'
  , NULL
  , '01-012'
  , 'Poland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    10999
  , 'OTTIK'
  , 6
  , '1996-05-03'
  , '1996-05-31'
  , '1996-05-10'
  , 2
  , 96.35
  , 'Ottilies Käseladen'
  , 'Mehrheimerstr. 369'
  , 'Köln'
  , NULL
  , '50739'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11000
  , 'RATTC'
  , 2
  , '1996-05-06'
  , '1996-06-03'
  , '1996-05-14'
  , 3
  , 55.12
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11001
  , 'FOLKO'
  , 2
  , '1996-05-06'
  , '1996-06-03'
  , '1996-05-14'
  , 2
  , 197.30
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11002
  , 'SAVEA'
  , 4
  , '1996-05-06'
  , '1996-06-03'
  , '1996-05-16'
  , 1
  , 141.16
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11003
  , 'THECR'
  , 3
  , '1996-05-06'
  , '1996-06-03'
  , '1996-05-08'
  , 3
  , 14.91
  , 'The Cracker Box'
  , '55 Grizzly Peak Rd.'
  , 'Butte'
  , 'MT'
  , '59801'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11004
  , 'MAISD'
  , 3
  , '1996-05-07'
  , '1996-06-04'
  , '1996-05-20'
  , 1
  , 44.84
  , 'Maison Dewey'
  , 'Rue Joseph-Bens 532'
  , 'Bruxelles'
  , NULL
  , 'B-1180'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11005
  , 'WILMK'
  , 2
  , '1996-05-07'
  , '1996-06-04'
  , '1996-05-10'
  , 1
  , 0.75
  , 'Wilman Kala'
  , 'Keskuskatu 45'
  , 'Helsinki'
  , NULL
  , '21240'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11006
  , 'GREAL'
  , 3
  , '1996-05-07'
  , '1996-06-04'
  , '1996-05-15'
  , 2
  , 25.19
  , 'Great Lakes Food Market'
  , '2732 Baker Blvd.'
  , 'Eugene'
  , 'OR'
  , '97403'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11007
  , 'PRINI'
  , 8
  , '1996-05-08'
  , '1996-06-05'
  , '1996-05-13'
  , 2
  , 202.24
  , 'Princesa Isabel Vinhos'
  , 'Estrada da saúde n. 58'
  , 'Lisboa'
  , NULL
  , '1756'
  , 'Portugal'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11008
  , 'ERNSH'
  , 7
  , '1996-05-08'
  , '1996-06-05'
  , NULL
  , 3
  , 79.46
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11009
  , 'GODOS'
  , 2
  , '1996-05-08'
  , '1996-06-05'
  , '1996-05-10'
  , 1
  , 59.11
  , 'Godos Cocina Típica'
  , 'C/ Romero, 33'
  , 'Sevilla'
  , NULL
  , '41101'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11010
  , 'REGGC'
  , 2
  , '1996-05-09'
  , '1996-06-06'
  , '1996-05-21'
  , 2
  , 28.71
  , 'Reggiani Caseifici'
  , 'Strada Provinciale 124'
  , 'Reggio Emilia'
  , NULL
  , '42100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11011
  , 'ALFKI'
  , 3
  , '1996-05-09'
  , '1996-06-06'
  , '1996-05-13'
  , 1
  , 1.21
  , 'Alfred''s Futterkiste'
  , 'Obere Str. 57'
  , 'Berlin'
  , NULL
  , '12209'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11012
  , 'FRANK'
  , 1
  , '1996-05-09'
  , '1996-05-23'
  , '1996-05-17'
  , 3
  , 242.95
  , 'Frankenversand'
  , 'Berliner Platz 43'
  , 'München'
  , NULL
  , '80805'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11013
  , 'ROMEY'
  , 2
  , '1996-05-09'
  , '1996-06-06'
  , '1996-05-10'
  , 1
  , 32.99
  , 'Romero y tomillo'
  , 'Gran Vía, 1'
  , 'Madrid'
  , NULL
  , '28001'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11014
  , 'LINOD'
  , 2
  , '1996-05-10'
  , '1996-06-07'
  , '1996-05-15'
  , 3
  , 23.60
  , 'LINO-Delicateses'
  , 'Ave. 5 de Mayo Porlamar'
  , 'I. de Margarita'
  , 'Nueva Esparta'
  , '4980'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11015
  , 'SANTG'
  , 2
  , '1996-05-10'
  , '1996-05-24'
  , '1996-05-20'
  , 2
  , 4.62
  , 'Sant?Gourmet'
  , 'Erling Skakkes gate 78'
  , 'Stavern'
  , NULL
  , '4110'
  , 'Norway'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11016
  , 'AROUT'
  , 9
  , '1996-05-10'
  , '1996-06-07'
  , '1996-05-13'
  , 2
  , 33.80
  , 'Around the Horn'
  , 'Brook Farm
Stratford St. Mary'
  , 'Colchester'
  , 'Essex'
  , 'CO7 6JX'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11017
  , 'ERNSH'
  , 9
  , '1996-05-13'
  , '1996-06-10'
  , '1996-05-20'
  , 2
  , 754.26
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11018
  , 'LONEP'
  , 4
  , '1996-05-13'
  , '1996-06-10'
  , '1996-05-16'
  , 2
  , 11.65
  , 'Lonesome Pine Restaurant'
  , '89 Chiaroscuro Rd.'
  , 'Portland'
  , 'OR'
  , '97219'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11019
  , 'RANCH'
  , 6
  , '1996-05-13'
  , '1996-06-10'
  , NULL
  , 3
  , 3.17
  , 'Rancho grande'
  , 'Av. del Libertador 900'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11020
  , 'OTTIK'
  , 2
  , '1996-05-14'
  , '1996-06-11'
  , '1996-05-16'
  , 2
  , 43.30
  , 'Ottilies Käseladen'
  , 'Mehrheimerstr. 369'
  , 'Köln'
  , NULL
  , '50739'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11021
  , 'QUICK'
  , 3
  , '1996-05-14'
  , '1996-06-11'
  , '1996-05-21'
  , 1
  , 297.18
  , 'QUICK-Stop'
  , 'Taucherstraße 10'
  , 'Cunewalde'
  , NULL
  , '01307'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11022
  , 'HANAR'
  , 9
  , '1996-05-14'
  , '1996-06-11'
  , '1996-06-03'
  , 2
  , 6.27
  , 'Hanari Carnes'
  , 'Rua do Paço, 67'
  , 'Rio de Janeiro'
  , 'RJ'
  , '05454-876'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11023
  , 'BSBEV'
  , 1
  , '1996-05-14'
  , '1996-05-28'
  , '1996-05-24'
  , 2
  , 123.83
  , 'B''s Beverages'
  , 'Fauntleroy Circus'
  , 'London'
  , NULL
  , 'EC2 5NT'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11024
  , 'EASTC'
  , 4
  , '1996-05-15'
  , '1996-06-12'
  , '1996-05-20'
  , 1
  , 74.36
  , 'Eastern Connection'
  , '35 King George'
  , 'London'
  , NULL
  , 'WX3 6FW'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11025
  , 'WARTH'
  , 6
  , '1996-05-15'
  , '1996-06-12'
  , '1996-05-24'
  , 3
  , 29.17
  , 'Wartian Herkku'
  , 'Torikatu 38'
  , 'Oulu'
  , NULL
  , '90110'
  , 'Finland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11026
  , 'FRANS'
  , 4
  , '1996-05-15'
  , '1996-06-12'
  , '1996-05-28'
  , 1
  , 47.09
  , 'Franchi S.p.A.'
  , 'Via Monte Bianco 34'
  , 'Torino'
  , NULL
  , '10100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11027
  , 'BOTTM'
  , 1
  , '1996-05-16'
  , '1996-06-13'
  , '1996-05-20'
  , 1
  , 52.52
  , 'Bottom-Dollar Markets'
  , '23 Tsawassen Blvd.'
  , 'Tsawassen'
  , 'BC'
  , 'T2F 8M4'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11028
  , 'KOENE'
  , 2
  , '1996-05-16'
  , '1996-06-13'
  , '1996-05-22'
  , 1
  , 29.59
  , 'Königlich Essen'
  , 'Maubelstr. 90'
  , 'Brandenburg'
  , NULL
  , '14776'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11029
  , 'CHOPS'
  , 4
  , '1996-05-16'
  , '1996-06-13'
  , '1996-05-27'
  , 1
  , 47.84
  , 'Chop-suey Chinese'
  , 'Hauptstr. 31'
  , 'Bern'
  , NULL
  , '3012'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11030
  , 'SAVEA'
  , 7
  , '1996-05-17'
  , '1996-06-14'
  , '1996-05-27'
  , 2
  , 830.75
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11031
  , 'SAVEA'
  , 6
  , '1996-05-17'
  , '1996-06-14'
  , '1996-05-24'
  , 2
  , 227.22
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11032
  , 'WHITC'
  , 2
  , '1996-05-17'
  , '1996-06-14'
  , '1996-05-23'
  , 3
  , 606.19
  , 'White Clover Markets'
  , '1029 - 12th Ave. S.'
  , 'Seattle'
  , 'WA'
  , '98124'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11033
  , 'RICSU'
  , 7
  , '1996-05-17'
  , '1996-06-14'
  , '1996-05-23'
  , 3
  , 84.74
  , 'Richter Supermarkt'
  , 'Starenweg 5'
  , 'Genève'
  , NULL
  , '1204'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11034
  , 'OLDWO'
  , 8
  , '1996-05-20'
  , '1996-07-01'
  , '1996-05-27'
  , 1
  , 40.32
  , 'Old World Delicatessen'
  , '2743 Bering St.'
  , 'Anchorage'
  , 'AK'
  , '99508'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11035
  , 'SUPRD'
  , 2
  , '1996-05-20'
  , '1996-06-17'
  , '1996-05-24'
  , 2
  , 0.17
  , 'Suprêmes délices'
  , 'Boulevard Tirou, 255'
  , 'Charleroi'
  , NULL
  , 'B-6000'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11036
  , 'DRACD'
  , 8
  , '1996-05-20'
  , '1996-06-17'
  , '1996-05-22'
  , 3
  , 149.47
  , 'Drachenblut Delikatessen'
  , 'Walserweg 21'
  , 'Aachen'
  , NULL
  , '52066'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11037
  , 'GODOS'
  , 7
  , '1996-05-21'
  , '1996-06-18'
  , '1996-05-27'
  , 1
  , 3.20
  , 'Godos Cocina Típica'
  , 'C/ Romero, 33'
  , 'Sevilla'
  , NULL
  , '41101'
  , 'Spain'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11038
  , 'SUPRD'
  , 1
  , '1996-05-21'
  , '1996-06-18'
  , '1996-05-30'
  , 2
  , 29.59
  , 'Suprêmes délices'
  , 'Boulevard Tirou, 255'
  , 'Charleroi'
  , NULL
  , 'B-6000'
  , 'Belgium'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11039
  , 'LINOD'
  , 1
  , '1996-05-21'
  , '1996-06-18'
  , NULL
  , 2
  , 65.00
  , 'LINO-Delicateses'
  , 'Ave. 5 de Mayo Porlamar'
  , 'I. de Margarita'
  , 'Nueva Esparta'
  , '4980'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11040
  , 'GREAL'
  , 4
  , '1996-05-22'
  , '1996-06-19'
  , NULL
  , 3
  , 18.84
  , 'Great Lakes Food Market'
  , '2732 Baker Blvd.'
  , 'Eugene'
  , 'OR'
  , '97403'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11041
  , 'CHOPS'
  , 3
  , '1996-05-22'
  , '1996-06-19'
  , '1996-05-28'
  , 2
  , 48.22
  , 'Chop-suey Chinese'
  , 'Hauptstr. 31'
  , 'Bern'
  , NULL
  , '3012'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11042
  , 'COMMI'
  , 2
  , '1996-05-22'
  , '1996-06-05'
  , '1996-05-31'
  , 1
  , 29.99
  , 'Comércio Mineiro'
  , 'Av. dos Lusíadas, 23'
  , 'São Paulo'
  , 'SP'
  , '05432-043'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11043
  , 'SPECD'
  , 5
  , '1996-05-22'
  , '1996-06-19'
  , '1996-05-29'
  , 2
  , 8.80
  , 'Spécialités du monde'
  , '25, rue Lauriston'
  , 'Paris'
  , NULL
  , '75016'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11044
  , 'WOLZA'
  , 4
  , '1996-05-23'
  , '1996-06-20'
  , '1996-05-31'
  , 1
  , 8.72
  , 'Wolski Zajazd'
  , 'ul. Filtrowa 68'
  , 'Warszawa'
  , NULL
  , '01-012'
  , 'Poland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11045
  , 'BOTTM'
  , 6
  , '1996-05-23'
  , '1996-06-20'
  , NULL
  , 2
  , 70.58
  , 'Bottom-Dollar Markets'
  , '23 Tsawassen Blvd.'
  , 'Tsawassen'
  , 'BC'
  , 'T2F 8M4'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11046
  , 'WANDK'
  , 8
  , '1996-05-23'
  , '1996-06-20'
  , '1996-05-24'
  , 2
  , 71.64
  , 'Die Wandernde Kuh'
  , 'Adenauerallee 900'
  , 'Stuttgart'
  , NULL
  , '70563'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11047
  , 'EASTC'
  , 7
  , '1996-05-24'
  , '1996-06-21'
  , '1996-05-31'
  , 3
  , 46.62
  , 'Eastern Connection'
  , '35 King George'
  , 'London'
  , NULL
  , 'WX3 6FW'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11048
  , 'BOTTM'
  , 7
  , '1996-05-24'
  , '1996-06-21'
  , '1996-05-30'
  , 3
  , 24.12
  , 'Bottom-Dollar Markets'
  , '23 Tsawassen Blvd.'
  , 'Tsawassen'
  , 'BC'
  , 'T2F 8M4'
  , 'Canada'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11049
  , 'GOURL'
  , 3
  , '1996-05-24'
  , '1996-06-21'
  , '1996-06-03'
  , 1
  , 8.34
  , 'Gourmet Lanchonetes'
  , 'Av. Brasil, 442'
  , 'Campinas'
  , 'SP'
  , '04876-786'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11050
  , 'FOLKO'
  , 8
  , '1996-05-27'
  , '1996-06-24'
  , '1996-06-04'
  , 2
  , 59.41
  , 'Folk och f?HB'
  , 'Åkergatan 24'
  , 'Bräcke'
  , NULL
  , 'S-844 67'
  , 'Sweden'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11051
  , 'LAMAI'
  , 7
  , '1996-05-27'
  , '1996-06-24'
  , NULL
  , 3
  , 2.79
  , 'La maison d''Asie'
  , '1 rue Alsace-Lorraine'
  , 'Toulouse'
  , NULL
  , '31000'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11052
  , 'HANAR'
  , 3
  , '1996-05-27'
  , '1996-06-24'
  , '1996-05-31'
  , 1
  , 67.26
  , 'Hanari Carnes'
  , 'Rua do Paço, 67'
  , 'Rio de Janeiro'
  , 'RJ'
  , '05454-876'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11053
  , 'PICCO'
  , 2
  , '1996-05-27'
  , '1996-06-24'
  , '1996-05-29'
  , 2
  , 53.05
  , 'Piccolo und mehr'
  , 'Geislweg 14'
  , 'Salzburg'
  , NULL
  , '5020'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11054
  , 'CACTU'
  , 8
  , '1996-05-28'
  , '1996-06-25'
  , NULL
  , 1
  , 0.33
  , 'Cactus Comidas para llevar'
  , 'Cerrito 333'
  , 'Buenos Aires'
  , NULL
  , '1010'
  , 'Argentina'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11055
  , 'HILAA'
  , 7
  , '1996-05-28'
  , '1996-06-25'
  , '1996-06-04'
  , 2
  , 120.92
  , 'HILARIÓN-Abastos'
  , 'Carrera 22 con Ave. Carlos Soublette #8-35'
  , 'San Cristóbal'
  , 'Táchira'
  , '5022'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11056
  , 'EASTC'
  , 8
  , '1996-05-28'
  , '1996-06-11'
  , '1996-05-31'
  , 2
  , 278.96
  , 'Eastern Connection'
  , '35 King George'
  , 'London'
  , NULL
  , 'WX3 6FW'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11057
  , 'NORTS'
  , 3
  , '1996-05-29'
  , '1996-06-26'
  , '1996-05-31'
  , 3
  , 4.13
  , 'North/South'
  , 'South House
300 Queensbridge'
  , 'London'
  , NULL
  , 'SW7 1RZ'
  , 'UK'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11058
  , 'BLAUS'
  , 9
  , '1996-05-29'
  , '1996-06-26'
  , NULL
  , 3
  , 31.14
  , 'Blauer See Delikatessen'
  , 'Forsterstr. 57'
  , 'Mannheim'
  , NULL
  , '68306'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11059
  , 'RICAR'
  , 2
  , '1996-05-29'
  , '1996-07-10'
  , NULL
  , 2
  , 85.80
  , 'Ricardo Adocicados'
  , 'Av. Copacabana, 267'
  , 'Rio de Janeiro'
  , 'RJ'
  , '02389-890'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11060
  , 'FRANS'
  , 2
  , '1996-05-30'
  , '1996-06-27'
  , '1996-06-03'
  , 2
  , 10.98
  , 'Franchi S.p.A.'
  , 'Via Monte Bianco 34'
  , 'Torino'
  , NULL
  , '10100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11061
  , 'GREAL'
  , 4
  , '1996-05-30'
  , '1996-07-11'
  , NULL
  , 3
  , 14.01
  , 'Great Lakes Food Market'
  , '2732 Baker Blvd.'
  , 'Eugene'
  , 'OR'
  , '97403'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11062
  , 'REGGC'
  , 4
  , '1996-05-30'
  , '1996-06-27'
  , NULL
  , 2
  , 29.93
  , 'Reggiani Caseifici'
  , 'Strada Provinciale 124'
  , 'Reggio Emilia'
  , NULL
  , '42100'
  , 'Italy'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11063
  , 'HUNGO'
  , 3
  , '1996-05-30'
  , '1996-06-27'
  , '1996-06-05'
  , 2
  , 81.73
  , 'Hungry Owl All-Night Grocers'
  , '8 Johnstown Road'
  , 'Cork'
  , 'Co. Cork'
  , NULL
  , 'Ireland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11064
  , 'SAVEA'
  , 1
  , '1996-05-31'
  , '1996-06-28'
  , '1996-06-03'
  , 1
  , 30.09
  , 'Save-a-lot Markets'
  , '187 Suffolk Ln.'
  , 'Boise'
  , 'ID'
  , '83720'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11065
  , 'LILAS'
  , 8
  , '1996-05-31'
  , '1996-06-28'
  , NULL
  , 1
  , 12.91
  , 'LILA-Supermercado'
  , 'Carrera 52 con Ave. Bolívar #65-98 Llano Largo'
  , 'Barquisimeto'
  , 'Lara'
  , '3508'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11066
  , 'WHITC'
  , 7
  , '1996-05-31'
  , '1996-06-28'
  , '1996-06-03'
  , 2
  , 44.72
  , 'White Clover Markets'
  , '1029 - 12th Ave. S.'
  , 'Seattle'
  , 'WA'
  , '98124'
  , 'USA'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11067
  , 'DRACD'
  , 1
  , '1996-06-03'
  , '1996-06-17'
  , '1996-06-05'
  , 2
  , 7.98
  , 'Drachenblut Delikatessen'
  , 'Walserweg 21'
  , 'Aachen'
  , NULL
  , '52066'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11068
  , 'QUEEN'
  , 8
  , '1996-06-03'
  , '1996-07-01'
  , NULL
  , 2
  , 81.75
  , 'Queen Cozinha'
  , 'Alameda dos Canàrios, 891'
  , 'São Paulo'
  , 'SP'
  , '05487-020'
  , 'Brazil'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11069
  , 'TORTU'
  , 1
  , '1996-06-03'
  , '1996-07-01'
  , '1996-06-05'
  , 2
  , 15.67
  , 'Tortuga Restaurante'
  , 'Avda. Azteca 123'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11070
  , 'LEHMS'
  , 2
  , '1996-06-04'
  , '1996-07-02'
  , NULL
  , 1
  , 136.00
  , 'Lehmanns Marktstand'
  , 'Magazinweg 7'
  , 'Frankfurt a.M. '
  , NULL
  , '60528'
  , 'Germany'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11071
  , 'LILAS'
  , 1
  , '1996-06-04'
  , '1996-07-02'
  , NULL
  , 1
  , 0.93
  , 'LILA-Supermercado'
  , 'Carrera 52 con Ave. Bolívar #65-98 Llano Largo'
  , 'Barquisimeto'
  , 'Lara'
  , '3508'
  , 'Venezuela'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11072
  , 'ERNSH'
  , 4
  , '1996-06-04'
  , '1996-07-02'
  , NULL
  , 2
  , 258.64
  , 'Ernst Handel'
  , 'Kirchgasse 6'
  , 'Graz'
  , NULL
  , '8010'
  , 'Austria'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11073
  , 'PERIC'
  , 2
  , '1996-06-04'
  , '1996-07-02'
  , NULL
  , 2
  , 24.95
  , 'Pericles Comidas clásicas'
  , 'Calle Dr. Jorge Cash 321'
  , 'México D.F.'
  , NULL
  , '05033'
  , 'Mexico'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11074
  , 'SIMOB'
  , 7
  , '1996-06-05'
  , '1996-07-03'
  , NULL
  , 2
  , 18.44
  , 'Simons bistro'
  , 'Vinbæltet 34'
  , 'København'
  , NULL
  , '1734'
  , 'Denmark'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11075
  , 'RICSU'
  , 8
  , '1996-06-05'
  , '1996-07-03'
  , NULL
  , 2
  , 6.19
  , 'Richter Supermarkt'
  , 'Starenweg 5'
  , 'Genève'
  , NULL
  , '1204'
  , 'Switzerland'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11076
  , 'BONAP'
  , 4
  , '1996-06-05'
  , '1996-07-03'
  , NULL
  , 2
  , 38.28
  , 'Bon app'''
  , '12, rue des Bouchers'
  , 'Marseille'
  , NULL
  , '13008'
  , 'France'
);

INSERT INTO ORDERS
(
    ID
  , CUSTOMER_ID
  , EMPLOYEE_ID
  , ORDER_DATE
  , REQUIRED_DATE
  , SHIPPED_DATE
  , SHIP_VIA
  , FREIGHT
  , SHIP_NAME
  , SHIP_ADDRESS
  , SHIP_CITY
  , SHIP_REGION
  , SHIP_POSTAL_CODE
  , SHIP_COUNTRY
)
VALUES
(
    11077
  , 'RATTC'
  , 1
  , '1996-06-05'
  , '1996-07-03'
  , NULL
  , 2
  , 8.53
  , 'Rattlesnake Canyon Grocery'
  , '2817 Milton Dr.'
  , 'Albuquerque'
  , 'NM'
  , '87110'
  , 'USA'
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1
  , 10248
  , 11
  , 14.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2
  , 10248
  , 42
  , 9.80
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    3
  , 10248
  , 72
  , 34.80
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    4
  , 10249
  , 14
  , 18.60
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    5
  , 10249
  , 51
  , 42.40
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    6
  , 10250
  , 41
  , 7.70
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    7
  , 10250
  , 51
  , 42.40
  , 35
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    8
  , 10250
  , 65
  , 16.80
  , 15
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    9
  , 10251
  , 22
  , 16.80
  , 6
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    10
  , 10251
  , 57
  , 15.60
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    11
  , 10251
  , 65
  , 16.80
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    12
  , 10252
  , 20
  , 64.80
  , 40
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    13
  , 10252
  , 33
  , 2.00
  , 25
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    14
  , 10252
  , 60
  , 27.20
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    15
  , 10253
  , 31
  , 10.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    16
  , 10253
  , 39
  , 14.40
  , 42
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    17
  , 10253
  , 49
  , 16.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    18
  , 10254
  , 24
  , 3.60
  , 15
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    19
  , 10254
  , 55
  , 19.20
  , 21
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    20
  , 10254
  , 74
  , 8.00
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    21
  , 10255
  , 2
  , 15.20
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    22
  , 10255
  , 16
  , 13.90
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    23
  , 10255
  , 36
  , 15.20
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    24
  , 10255
  , 59
  , 44.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    25
  , 10256
  , 53
  , 26.20
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    26
  , 10256
  , 77
  , 10.40
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    27
  , 10257
  , 27
  , 35.10
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    28
  , 10257
  , 39
  , 14.40
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    29
  , 10257
  , 77
  , 10.40
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    30
  , 10258
  , 2
  , 15.20
  , 50
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    31
  , 10258
  , 5
  , 17.00
  , 65
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    32
  , 10258
  , 32
  , 25.60
  , 6
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    33
  , 10259
  , 21
  , 8.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    34
  , 10259
  , 37
  , 20.80
  , 1
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    35
  , 10260
  , 41
  , 7.70
  , 16
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    36
  , 10260
  , 57
  , 15.60
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    37
  , 10260
  , 62
  , 39.40
  , 15
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    38
  , 10260
  , 70
  , 12.00
  , 21
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    39
  , 10261
  , 21
  , 8.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    40
  , 10261
  , 35
  , 14.40
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    41
  , 10262
  , 5
  , 17.00
  , 12
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    42
  , 10262
  , 7
  , 24.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    43
  , 10262
  , 56
  , 30.40
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    44
  , 10263
  , 16
  , 13.90
  , 60
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    45
  , 10263
  , 24
  , 3.60
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    46
  , 10263
  , 30
  , 20.70
  , 60
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    47
  , 10263
  , 74
  , 8.00
  , 36
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    48
  , 10264
  , 2
  , 15.20
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    49
  , 10264
  , 41
  , 7.70
  , 25
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    50
  , 10265
  , 17
  , 31.20
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    51
  , 10265
  , 70
  , 12.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    52
  , 10266
  , 12
  , 30.40
  , 12
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    53
  , 10267
  , 40
  , 14.70
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    54
  , 10267
  , 59
  , 44.00
  , 70
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    55
  , 10267
  , 76
  , 14.40
  , 15
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    56
  , 10268
  , 29
  , 99.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    57
  , 10268
  , 72
  , 27.80
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    58
  , 10269
  , 33
  , 2.00
  , 60
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    59
  , 10269
  , 72
  , 27.80
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    60
  , 10270
  , 36
  , 15.20
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    61
  , 10270
  , 43
  , 36.80
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    62
  , 10271
  , 33
  , 2.00
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    63
  , 10272
  , 20
  , 64.80
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    64
  , 10272
  , 31
  , 10.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    65
  , 10272
  , 72
  , 27.80
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    66
  , 10273
  , 10
  , 24.80
  , 24
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    67
  , 10273
  , 31
  , 10.00
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    68
  , 10273
  , 33
  , 2.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    69
  , 10273
  , 40
  , 14.70
  , 60
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    70
  , 10273
  , 76
  , 14.40
  , 33
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    71
  , 10274
  , 71
  , 17.20
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    72
  , 10274
  , 72
  , 27.80
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    73
  , 10275
  , 24
  , 3.60
  , 12
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    74
  , 10275
  , 59
  , 44.00
  , 6
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    75
  , 10276
  , 10
  , 24.80
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    76
  , 10276
  , 13
  , 4.80
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    77
  , 10277
  , 28
  , 36.40
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    78
  , 10277
  , 62
  , 39.40
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    79
  , 10278
  , 44
  , 15.50
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    80
  , 10278
  , 59
  , 44.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    81
  , 10278
  , 63
  , 35.10
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    82
  , 10278
  , 73
  , 12.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    83
  , 10279
  , 17
  , 31.20
  , 15
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    84
  , 10280
  , 24
  , 3.60
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    85
  , 10280
  , 55
  , 19.20
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    86
  , 10280
  , 75
  , 6.20
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    87
  , 10281
  , 19
  , 7.30
  , 1
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    88
  , 10281
  , 24
  , 3.60
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    89
  , 10281
  , 35
  , 14.40
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    90
  , 10282
  , 30
  , 20.70
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    91
  , 10282
  , 57
  , 15.60
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    92
  , 10283
  , 15
  , 12.40
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    93
  , 10283
  , 19
  , 7.30
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    94
  , 10283
  , 60
  , 27.20
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    95
  , 10283
  , 72
  , 27.80
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    96
  , 10284
  , 27
  , 35.10
  , 15
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    97
  , 10284
  , 44
  , 15.50
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    98
  , 10284
  , 60
  , 27.20
  , 20
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    99
  , 10284
  , 67
  , 11.20
  , 5
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    100
  , 10285
  , 1
  , 14.40
  , 45
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    101
  , 10285
  , 40
  , 14.70
  , 40
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    102
  , 10285
  , 53
  , 26.20
  , 36
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    103
  , 10286
  , 35
  , 14.40
  , 100
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    104
  , 10286
  , 62
  , 39.40
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    105
  , 10287
  , 16
  , 13.90
  , 40
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    106
  , 10287
  , 34
  , 11.20
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    107
  , 10287
  , 46
  , 9.60
  , 15
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    108
  , 10288
  , 54
  , 5.90
  , 10
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    109
  , 10288
  , 68
  , 10.00
  , 3
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    110
  , 10289
  , 3
  , 8.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    111
  , 10289
  , 64
  , 26.60
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    112
  , 10290
  , 5
  , 17.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    113
  , 10290
  , 29
  , 99.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    114
  , 10290
  , 49
  , 16.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    115
  , 10290
  , 77
  , 10.40
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    116
  , 10291
  , 13
  , 4.80
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    117
  , 10291
  , 44
  , 15.50
  , 24
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    118
  , 10291
  , 51
  , 42.40
  , 2
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    119
  , 10292
  , 20
  , 64.80
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    120
  , 10293
  , 18
  , 50.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    121
  , 10293
  , 24
  , 3.60
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    122
  , 10293
  , 63
  , 35.10
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    123
  , 10293
  , 75
  , 6.20
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    124
  , 10294
  , 1
  , 14.40
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    125
  , 10294
  , 17
  , 31.20
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    126
  , 10294
  , 43
  , 36.80
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    127
  , 10294
  , 60
  , 27.20
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    128
  , 10294
  , 75
  , 6.20
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    129
  , 10295
  , 56
  , 30.40
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    130
  , 10296
  , 11
  , 16.80
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    131
  , 10296
  , 16
  , 13.90
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    132
  , 10296
  , 69
  , 28.80
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    133
  , 10297
  , 39
  , 14.40
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    134
  , 10297
  , 72
  , 27.80
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    135
  , 10298
  , 2
  , 15.20
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    136
  , 10298
  , 36
  , 15.20
  , 40
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    137
  , 10298
  , 59
  , 44.00
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    138
  , 10298
  , 62
  , 39.40
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    139
  , 10299
  , 19
  , 7.30
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    140
  , 10299
  , 70
  , 12.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    141
  , 10300
  , 66
  , 13.60
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    142
  , 10300
  , 68
  , 10.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    143
  , 10301
  , 40
  , 14.70
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    144
  , 10301
  , 56
  , 30.40
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    145
  , 10302
  , 17
  , 31.20
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    146
  , 10302
  , 28
  , 36.40
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    147
  , 10302
  , 43
  , 36.80
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    148
  , 10303
  , 40
  , 14.70
  , 40
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    149
  , 10303
  , 65
  , 16.80
  , 30
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    150
  , 10303
  , 68
  , 10.00
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    151
  , 10304
  , 49
  , 16.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    152
  , 10304
  , 59
  , 44.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    153
  , 10304
  , 71
  , 17.20
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    154
  , 10305
  , 18
  , 50.00
  , 25
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    155
  , 10305
  , 29
  , 99.00
  , 25
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    156
  , 10305
  , 39
  , 14.40
  , 30
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    157
  , 10306
  , 30
  , 20.70
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    158
  , 10306
  , 53
  , 26.20
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    159
  , 10306
  , 54
  , 5.90
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    160
  , 10307
  , 62
  , 39.40
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    161
  , 10307
  , 68
  , 10.00
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    162
  , 10308
  , 69
  , 28.80
  , 1
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    163
  , 10308
  , 70
  , 12.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    164
  , 10309
  , 4
  , 17.60
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    165
  , 10309
  , 6
  , 20.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    166
  , 10309
  , 42
  , 11.20
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    167
  , 10309
  , 43
  , 36.80
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    168
  , 10309
  , 71
  , 17.20
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    169
  , 10310
  , 16
  , 13.90
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    170
  , 10310
  , 62
  , 39.40
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    171
  , 10311
  , 42
  , 11.20
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    172
  , 10311
  , 69
  , 28.80
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    173
  , 10312
  , 28
  , 36.40
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    174
  , 10312
  , 43
  , 36.80
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    175
  , 10312
  , 53
  , 26.20
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    176
  , 10312
  , 75
  , 6.20
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    177
  , 10313
  , 36
  , 15.20
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    178
  , 10314
  , 32
  , 25.60
  , 40
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    179
  , 10314
  , 58
  , 10.60
  , 30
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    180
  , 10314
  , 62
  , 39.40
  , 25
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    181
  , 10315
  , 34
  , 11.20
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    182
  , 10315
  , 70
  , 12.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    183
  , 10316
  , 41
  , 7.70
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    184
  , 10316
  , 62
  , 39.40
  , 70
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    185
  , 10317
  , 1
  , 14.40
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    186
  , 10318
  , 41
  , 7.70
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    187
  , 10318
  , 76
  , 14.40
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    188
  , 10319
  , 17
  , 31.20
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    189
  , 10319
  , 28
  , 36.40
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    190
  , 10319
  , 76
  , 14.40
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    191
  , 10320
  , 71
  , 17.20
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    192
  , 10321
  , 35
  , 14.40
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    193
  , 10322
  , 52
  , 5.60
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    194
  , 10323
  , 15
  , 12.40
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    195
  , 10323
  , 25
  , 11.20
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    196
  , 10323
  , 39
  , 14.40
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    197
  , 10324
  , 16
  , 13.90
  , 21
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    198
  , 10324
  , 35
  , 14.40
  , 70
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    199
  , 10324
  , 46
  , 9.60
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    200
  , 10324
  , 59
  , 44.00
  , 40
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    201
  , 10324
  , 63
  , 35.10
  , 80
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    202
  , 10325
  , 6
  , 20.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    203
  , 10325
  , 13
  , 4.80
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    204
  , 10325
  , 14
  , 18.60
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    205
  , 10325
  , 31
  , 10.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    206
  , 10325
  , 72
  , 27.80
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    207
  , 10326
  , 4
  , 17.60
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    208
  , 10326
  , 57
  , 15.60
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    209
  , 10326
  , 75
  , 6.20
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    210
  , 10327
  , 2
  , 15.20
  , 25
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    211
  , 10327
  , 11
  , 16.80
  , 50
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    212
  , 10327
  , 30
  , 20.70
  , 35
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    213
  , 10327
  , 58
  , 10.60
  , 30
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    214
  , 10328
  , 59
  , 44.00
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    215
  , 10328
  , 65
  , 16.80
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    216
  , 10328
  , 68
  , 10.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    217
  , 10329
  , 19
  , 7.30
  , 10
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    218
  , 10329
  , 30
  , 20.70
  , 8
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    219
  , 10329
  , 38
  , 210.80
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    220
  , 10329
  , 56
  , 30.40
  , 12
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    221
  , 10330
  , 26
  , 24.90
  , 50
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    222
  , 10330
  , 72
  , 27.80
  , 25
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    223
  , 10331
  , 54
  , 5.90
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    224
  , 10332
  , 18
  , 50.00
  , 40
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    225
  , 10332
  , 42
  , 11.20
  , 10
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    226
  , 10332
  , 47
  , 7.60
  , 16
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    227
  , 10333
  , 14
  , 18.60
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    228
  , 10333
  , 21
  , 8.00
  , 10
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    229
  , 10333
  , 71
  , 17.20
  , 40
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    230
  , 10334
  , 52
  , 5.60
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    231
  , 10334
  , 68
  , 10.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    232
  , 10335
  , 2
  , 15.20
  , 7
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    233
  , 10335
  , 31
  , 10.00
  , 25
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    234
  , 10335
  , 32
  , 25.60
  , 6
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    235
  , 10335
  , 51
  , 42.40
  , 48
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    236
  , 10336
  , 4
  , 17.60
  , 18
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    237
  , 10337
  , 23
  , 7.20
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    238
  , 10337
  , 26
  , 24.90
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    239
  , 10337
  , 36
  , 15.20
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    240
  , 10337
  , 37
  , 20.80
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    241
  , 10337
  , 72
  , 27.80
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    242
  , 10338
  , 17
  , 31.20
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    243
  , 10338
  , 30
  , 20.70
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    244
  , 10339
  , 4
  , 17.60
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    245
  , 10339
  , 17
  , 31.20
  , 70
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    246
  , 10339
  , 62
  , 39.40
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    247
  , 10340
  , 18
  , 50.00
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    248
  , 10340
  , 41
  , 7.70
  , 12
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    249
  , 10340
  , 43
  , 36.80
  , 40
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    250
  , 10341
  , 33
  , 2.00
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    251
  , 10341
  , 59
  , 44.00
  , 9
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    252
  , 10342
  , 2
  , 15.20
  , 24
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    253
  , 10342
  , 31
  , 10.00
  , 56
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    254
  , 10342
  , 36
  , 15.20
  , 40
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    255
  , 10342
  , 55
  , 19.20
  , 40
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    256
  , 10343
  , 64
  , 26.60
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    257
  , 10343
  , 68
  , 10.00
  , 4
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    258
  , 10343
  , 76
  , 14.40
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    259
  , 10344
  , 4
  , 17.60
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    260
  , 10344
  , 8
  , 32.00
  , 70
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    261
  , 10345
  , 8
  , 32.00
  , 70
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    262
  , 10345
  , 19
  , 7.30
  , 80
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    263
  , 10345
  , 42
  , 11.20
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    264
  , 10346
  , 17
  , 31.20
  , 36
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    265
  , 10346
  , 56
  , 30.40
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    266
  , 10347
  , 25
  , 11.20
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    267
  , 10347
  , 39
  , 14.40
  , 50
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    268
  , 10347
  , 40
  , 14.70
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    269
  , 10347
  , 75
  , 6.20
  , 6
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    270
  , 10348
  , 1
  , 14.40
  , 15
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    271
  , 10348
  , 23
  , 7.20
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    272
  , 10349
  , 54
  , 5.90
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    273
  , 10350
  , 50
  , 13.00
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    274
  , 10350
  , 69
  , 28.80
  , 18
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    275
  , 10351
  , 38
  , 210.80
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    276
  , 10351
  , 41
  , 7.70
  , 13
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    277
  , 10351
  , 44
  , 15.50
  , 77
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    278
  , 10351
  , 65
  , 16.80
  , 10
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    279
  , 10352
  , 24
  , 3.60
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    280
  , 10352
  , 54
  , 5.90
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    281
  , 10353
  , 11
  , 16.80
  , 12
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    282
  , 10353
  , 38
  , 210.80
  , 50
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    283
  , 10354
  , 1
  , 14.40
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    284
  , 10354
  , 29
  , 99.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    285
  , 10355
  , 24
  , 3.60
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    286
  , 10355
  , 57
  , 15.60
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    287
  , 10356
  , 31
  , 10.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    288
  , 10356
  , 55
  , 19.20
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    289
  , 10356
  , 69
  , 28.80
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    290
  , 10357
  , 10
  , 24.80
  , 30
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    291
  , 10357
  , 26
  , 24.90
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    292
  , 10357
  , 60
  , 27.20
  , 8
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    293
  , 10358
  , 24
  , 3.60
  , 10
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    294
  , 10358
  , 34
  , 11.20
  , 10
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    295
  , 10358
  , 36
  , 15.20
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    296
  , 10359
  , 16
  , 13.90
  , 56
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    297
  , 10359
  , 31
  , 10.00
  , 70
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    298
  , 10359
  , 60
  , 27.20
  , 80
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    299
  , 10360
  , 28
  , 36.40
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    300
  , 10360
  , 29
  , 99.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    301
  , 10360
  , 38
  , 210.80
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    302
  , 10360
  , 49
  , 16.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    303
  , 10360
  , 54
  , 5.90
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    304
  , 10361
  , 39
  , 14.40
  , 54
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    305
  , 10361
  , 60
  , 27.20
  , 55
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    306
  , 10362
  , 25
  , 11.20
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    307
  , 10362
  , 51
  , 42.40
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    308
  , 10362
  , 54
  , 5.90
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    309
  , 10363
  , 31
  , 10.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    310
  , 10363
  , 75
  , 6.20
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    311
  , 10363
  , 76
  , 14.40
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    312
  , 10364
  , 69
  , 28.80
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    313
  , 10364
  , 71
  , 17.20
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    314
  , 10365
  , 11
  , 16.80
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    315
  , 10366
  , 65
  , 16.80
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    316
  , 10366
  , 77
  , 10.40
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    317
  , 10367
  , 34
  , 11.20
  , 36
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    318
  , 10367
  , 54
  , 5.90
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    319
  , 10367
  , 65
  , 16.80
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    320
  , 10367
  , 77
  , 10.40
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    321
  , 10368
  , 21
  , 8.00
  , 5
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    322
  , 10368
  , 28
  , 36.40
  , 13
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    323
  , 10368
  , 57
  , 15.60
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    324
  , 10368
  , 64
  , 26.60
  , 35
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    325
  , 10369
  , 29
  , 99.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    326
  , 10369
  , 56
  , 30.40
  , 18
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    327
  , 10370
  , 1
  , 14.40
  , 15
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    328
  , 10370
  , 64
  , 26.60
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    329
  , 10370
  , 74
  , 8.00
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    330
  , 10371
  , 36
  , 15.20
  , 6
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    331
  , 10372
  , 20
  , 64.80
  , 12
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    332
  , 10372
  , 38
  , 210.80
  , 40
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    333
  , 10372
  , 60
  , 27.20
  , 70
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    334
  , 10372
  , 72
  , 27.80
  , 42
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    335
  , 10373
  , 58
  , 10.60
  , 80
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    336
  , 10373
  , 71
  , 17.20
  , 50
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    337
  , 10374
  , 31
  , 10.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    338
  , 10374
  , 58
  , 10.60
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    339
  , 10375
  , 14
  , 18.60
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    340
  , 10375
  , 54
  , 5.90
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    341
  , 10376
  , 31
  , 10.00
  , 42
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    342
  , 10377
  , 28
  , 36.40
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    343
  , 10377
  , 39
  , 14.40
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    344
  , 10378
  , 71
  , 17.20
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    345
  , 10379
  , 41
  , 7.70
  , 8
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    346
  , 10379
  , 63
  , 35.10
  , 16
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    347
  , 10379
  , 65
  , 16.80
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    348
  , 10380
  , 30
  , 20.70
  , 18
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    349
  , 10380
  , 53
  , 26.20
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    350
  , 10380
  , 60
  , 27.20
  , 6
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    351
  , 10380
  , 70
  , 12.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    352
  , 10381
  , 74
  , 8.00
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    353
  , 10382
  , 5
  , 17.00
  , 32
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    354
  , 10382
  , 18
  , 50.00
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    355
  , 10382
  , 29
  , 99.00
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    356
  , 10382
  , 33
  , 2.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    357
  , 10382
  , 74
  , 8.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    358
  , 10383
  , 13
  , 4.80
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    359
  , 10383
  , 50
  , 13.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    360
  , 10383
  , 56
  , 30.40
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    361
  , 10384
  , 20
  , 64.80
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    362
  , 10384
  , 60
  , 27.20
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    363
  , 10385
  , 7
  , 24.00
  , 10
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    364
  , 10385
  , 60
  , 27.20
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    365
  , 10385
  , 68
  , 10.00
  , 8
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    366
  , 10386
  , 24
  , 3.60
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    367
  , 10386
  , 34
  , 11.20
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    368
  , 10387
  , 24
  , 3.60
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    369
  , 10387
  , 28
  , 36.40
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    370
  , 10387
  , 59
  , 44.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    371
  , 10387
  , 71
  , 17.20
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    372
  , 10388
  , 45
  , 7.60
  , 15
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    373
  , 10388
  , 52
  , 5.60
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    374
  , 10388
  , 53
  , 26.20
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    375
  , 10389
  , 10
  , 24.80
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    376
  , 10389
  , 55
  , 19.20
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    377
  , 10389
  , 62
  , 39.40
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    378
  , 10389
  , 70
  , 12.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    379
  , 10390
  , 31
  , 10.00
  , 60
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    380
  , 10390
  , 35
  , 14.40
  , 40
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    381
  , 10390
  , 46
  , 9.60
  , 45
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    382
  , 10390
  , 72
  , 27.80
  , 24
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    383
  , 10391
  , 13
  , 4.80
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    384
  , 10392
  , 69
  , 28.80
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    385
  , 10393
  , 2
  , 15.20
  , 25
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    386
  , 10393
  , 14
  , 18.60
  , 42
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    387
  , 10393
  , 25
  , 11.20
  , 7
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    388
  , 10393
  , 26
  , 24.90
  , 70
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    389
  , 10393
  , 31
  , 10.00
  , 32
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    390
  , 10394
  , 13
  , 4.80
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    391
  , 10394
  , 62
  , 39.40
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    392
  , 10395
  , 46
  , 9.60
  , 28
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    393
  , 10395
  , 53
  , 26.20
  , 70
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    394
  , 10395
  , 69
  , 28.80
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    395
  , 10396
  , 23
  , 7.20
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    396
  , 10396
  , 71
  , 17.20
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    397
  , 10396
  , 72
  , 27.80
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    398
  , 10397
  , 21
  , 8.00
  , 10
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    399
  , 10397
  , 51
  , 42.40
  , 18
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    400
  , 10398
  , 35
  , 14.40
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    401
  , 10398
  , 55
  , 19.20
  , 120
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    402
  , 10399
  , 68
  , 10.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    403
  , 10399
  , 71
  , 17.20
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    404
  , 10399
  , 76
  , 14.40
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    405
  , 10399
  , 77
  , 10.40
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    406
  , 10400
  , 29
  , 99.00
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    407
  , 10400
  , 35
  , 14.40
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    408
  , 10400
  , 49
  , 16.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    409
  , 10401
  , 30
  , 20.70
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    410
  , 10401
  , 56
  , 30.40
  , 70
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    411
  , 10401
  , 65
  , 16.80
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    412
  , 10401
  , 71
  , 17.20
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    413
  , 10402
  , 23
  , 7.20
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    414
  , 10402
  , 63
  , 35.10
  , 65
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    415
  , 10403
  , 16
  , 13.90
  , 21
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    416
  , 10403
  , 48
  , 10.20
  , 70
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    417
  , 10404
  , 26
  , 24.90
  , 30
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    418
  , 10404
  , 42
  , 11.20
  , 40
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    419
  , 10404
  , 49
  , 16.00
  , 30
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    420
  , 10405
  , 3
  , 8.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    421
  , 10406
  , 1
  , 14.40
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    422
  , 10406
  , 21
  , 8.00
  , 30
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    423
  , 10406
  , 28
  , 36.40
  , 42
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    424
  , 10406
  , 36
  , 15.20
  , 5
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    425
  , 10406
  , 40
  , 14.70
  , 2
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    426
  , 10407
  , 11
  , 16.80
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    427
  , 10407
  , 69
  , 28.80
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    428
  , 10407
  , 71
  , 17.20
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    429
  , 10408
  , 37
  , 20.80
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    430
  , 10408
  , 54
  , 5.90
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    431
  , 10408
  , 62
  , 39.40
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    432
  , 10409
  , 14
  , 18.60
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    433
  , 10409
  , 21
  , 8.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    434
  , 10410
  , 33
  , 2.00
  , 49
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    435
  , 10410
  , 59
  , 44.00
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    436
  , 10411
  , 41
  , 7.70
  , 25
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    437
  , 10411
  , 44
  , 15.50
  , 40
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    438
  , 10411
  , 59
  , 44.00
  , 9
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    439
  , 10412
  , 14
  , 18.60
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    440
  , 10413
  , 1
  , 14.40
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    441
  , 10413
  , 62
  , 39.40
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    442
  , 10413
  , 76
  , 14.40
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    443
  , 10414
  , 19
  , 7.30
  , 18
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    444
  , 10414
  , 33
  , 2.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    445
  , 10415
  , 17
  , 31.20
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    446
  , 10415
  , 33
  , 2.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    447
  , 10416
  , 19
  , 7.30
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    448
  , 10416
  , 53
  , 26.20
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    449
  , 10416
  , 57
  , 15.60
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    450
  , 10417
  , 38
  , 210.80
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    451
  , 10417
  , 46
  , 9.60
  , 2
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    452
  , 10417
  , 68
  , 10.00
  , 36
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    453
  , 10417
  , 77
  , 10.40
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    454
  , 10418
  , 2
  , 15.20
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    455
  , 10418
  , 47
  , 7.60
  , 55
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    456
  , 10418
  , 61
  , 22.80
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    457
  , 10418
  , 74
  , 8.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    458
  , 10419
  , 60
  , 27.20
  , 60
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    459
  , 10419
  , 69
  , 28.80
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    460
  , 10420
  , 9
  , 77.60
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    461
  , 10420
  , 13
  , 4.80
  , 2
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    462
  , 10420
  , 70
  , 12.00
  , 8
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    463
  , 10420
  , 73
  , 12.00
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    464
  , 10421
  , 19
  , 7.30
  , 4
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    465
  , 10421
  , 26
  , 24.90
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    466
  , 10421
  , 53
  , 26.20
  , 15
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    467
  , 10421
  , 77
  , 10.40
  , 10
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    468
  , 10422
  , 26
  , 24.90
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    469
  , 10423
  , 31
  , 10.00
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    470
  , 10423
  , 59
  , 44.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    471
  , 10424
  , 35
  , 14.40
  , 60
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    472
  , 10424
  , 38
  , 210.80
  , 49
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    473
  , 10424
  , 68
  , 10.00
  , 30
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    474
  , 10425
  , 55
  , 19.20
  , 10
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    475
  , 10425
  , 76
  , 14.40
  , 20
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    476
  , 10426
  , 56
  , 30.40
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    477
  , 10426
  , 64
  , 26.60
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    478
  , 10427
  , 14
  , 18.60
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    479
  , 10428
  , 46
  , 9.60
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    480
  , 10429
  , 50
  , 13.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    481
  , 10429
  , 63
  , 35.10
  , 35
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    482
  , 10430
  , 17
  , 31.20
  , 45
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    483
  , 10430
  , 21
  , 8.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    484
  , 10430
  , 56
  , 30.40
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    485
  , 10430
  , 59
  , 44.00
  , 70
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    486
  , 10431
  , 17
  , 31.20
  , 50
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    487
  , 10431
  , 40
  , 14.70
  , 50
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    488
  , 10431
  , 47
  , 7.60
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    489
  , 10432
  , 26
  , 24.90
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    490
  , 10432
  , 54
  , 5.90
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    491
  , 10433
  , 56
  , 30.40
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    492
  , 10434
  , 11
  , 16.80
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    493
  , 10434
  , 76
  , 14.40
  , 18
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    494
  , 10435
  , 2
  , 15.20
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    495
  , 10435
  , 22
  , 16.80
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    496
  , 10435
  , 72
  , 27.80
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    497
  , 10436
  , 46
  , 9.60
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    498
  , 10436
  , 56
  , 30.40
  , 40
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    499
  , 10436
  , 64
  , 26.60
  , 30
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    500
  , 10436
  , 75
  , 6.20
  , 24
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    501
  , 10437
  , 53
  , 26.20
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    502
  , 10438
  , 19
  , 7.30
  , 15
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    503
  , 10438
  , 34
  , 11.20
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    504
  , 10438
  , 57
  , 15.60
  , 15
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    505
  , 10439
  , 12
  , 30.40
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    506
  , 10439
  , 16
  , 13.90
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    507
  , 10439
  , 64
  , 26.60
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    508
  , 10439
  , 74
  , 8.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    509
  , 10440
  , 2
  , 15.20
  , 45
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    510
  , 10440
  , 16
  , 13.90
  , 49
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    511
  , 10440
  , 29
  , 99.00
  , 24
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    512
  , 10440
  , 61
  , 22.80
  , 90
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    513
  , 10441
  , 27
  , 35.10
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    514
  , 10442
  , 11
  , 16.80
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    515
  , 10442
  , 54
  , 5.90
  , 80
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    516
  , 10442
  , 66
  , 13.60
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    517
  , 10443
  , 11
  , 16.80
  , 6
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    518
  , 10443
  , 28
  , 36.40
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    519
  , 10444
  , 17
  , 31.20
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    520
  , 10444
  , 26
  , 24.90
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    521
  , 10444
  , 35
  , 14.40
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    522
  , 10444
  , 41
  , 7.70
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    523
  , 10445
  , 39
  , 14.40
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    524
  , 10445
  , 54
  , 5.90
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    525
  , 10446
  , 19
  , 7.30
  , 12
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    526
  , 10446
  , 24
  , 3.60
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    527
  , 10446
  , 31
  , 10.00
  , 3
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    528
  , 10446
  , 52
  , 5.60
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    529
  , 10447
  , 19
  , 7.30
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    530
  , 10447
  , 65
  , 16.80
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    531
  , 10447
  , 71
  , 17.20
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    532
  , 10448
  , 26
  , 24.90
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    533
  , 10448
  , 40
  , 14.70
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    534
  , 10449
  , 10
  , 24.80
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    535
  , 10449
  , 52
  , 5.60
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    536
  , 10449
  , 62
  , 39.40
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    537
  , 10450
  , 10
  , 24.80
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    538
  , 10450
  , 54
  , 5.90
  , 6
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    539
  , 10451
  , 55
  , 19.20
  , 120
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    540
  , 10451
  , 64
  , 26.60
  , 35
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    541
  , 10451
  , 65
  , 16.80
  , 28
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    542
  , 10451
  , 77
  , 10.40
  , 55
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    543
  , 10452
  , 28
  , 36.40
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    544
  , 10452
  , 44
  , 15.50
  , 100
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    545
  , 10453
  , 48
  , 10.20
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    546
  , 10453
  , 70
  , 12.00
  , 25
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    547
  , 10454
  , 16
  , 13.90
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    548
  , 10454
  , 33
  , 2.00
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    549
  , 10454
  , 46
  , 9.60
  , 10
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    550
  , 10455
  , 39
  , 14.40
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    551
  , 10455
  , 53
  , 26.20
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    552
  , 10455
  , 61
  , 22.80
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    553
  , 10455
  , 71
  , 17.20
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    554
  , 10456
  , 21
  , 8.00
  , 40
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    555
  , 10456
  , 49
  , 16.00
  , 21
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    556
  , 10457
  , 59
  , 44.00
  , 36
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    557
  , 10458
  , 26
  , 24.90
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    558
  , 10458
  , 28
  , 36.40
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    559
  , 10458
  , 43
  , 36.80
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    560
  , 10458
  , 56
  , 30.40
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    561
  , 10458
  , 71
  , 17.20
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    562
  , 10459
  , 7
  , 24.00
  , 16
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    563
  , 10459
  , 46
  , 9.60
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    564
  , 10459
  , 72
  , 27.80
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    565
  , 10460
  , 68
  , 10.00
  , 21
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    566
  , 10460
  , 75
  , 6.20
  , 4
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    567
  , 10461
  , 21
  , 8.00
  , 40
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    568
  , 10461
  , 30
  , 20.70
  , 28
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    569
  , 10461
  , 55
  , 19.20
  , 60
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    570
  , 10462
  , 13
  , 4.80
  , 1
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    571
  , 10462
  , 23
  , 7.20
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    572
  , 10463
  , 19
  , 7.30
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    573
  , 10463
  , 42
  , 11.20
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    574
  , 10464
  , 4
  , 17.60
  , 16
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    575
  , 10464
  , 43
  , 36.80
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    576
  , 10464
  , 56
  , 30.40
  , 30
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    577
  , 10464
  , 60
  , 27.20
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    578
  , 10465
  , 24
  , 3.60
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    579
  , 10465
  , 29
  , 99.00
  , 18
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    580
  , 10465
  , 40
  , 14.70
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    581
  , 10465
  , 45
  , 7.60
  , 30
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    582
  , 10465
  , 50
  , 13.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    583
  , 10466
  , 11
  , 16.80
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    584
  , 10466
  , 46
  , 9.60
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    585
  , 10467
  , 24
  , 3.60
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    586
  , 10467
  , 25
  , 11.20
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    587
  , 10468
  , 30
  , 20.70
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    588
  , 10468
  , 43
  , 36.80
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    589
  , 10469
  , 2
  , 15.20
  , 40
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    590
  , 10469
  , 16
  , 13.90
  , 35
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    591
  , 10469
  , 44
  , 15.50
  , 2
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    592
  , 10470
  , 18
  , 50.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    593
  , 10470
  , 23
  , 7.20
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    594
  , 10470
  , 64
  , 26.60
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    595
  , 10471
  , 7
  , 24.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    596
  , 10471
  , 56
  , 30.40
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    597
  , 10472
  , 24
  , 3.60
  , 80
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    598
  , 10472
  , 51
  , 42.40
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    599
  , 10473
  , 33
  , 2.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    600
  , 10473
  , 71
  , 17.20
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    601
  , 10474
  , 14
  , 18.60
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    602
  , 10474
  , 28
  , 36.40
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    603
  , 10474
  , 40
  , 14.70
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    604
  , 10474
  , 75
  , 6.20
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    605
  , 10475
  , 31
  , 10.00
  , 35
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    606
  , 10475
  , 66
  , 13.60
  , 60
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    607
  , 10475
  , 76
  , 14.40
  , 42
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    608
  , 10476
  , 55
  , 19.20
  , 2
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    609
  , 10476
  , 70
  , 12.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    610
  , 10477
  , 1
  , 14.40
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    611
  , 10477
  , 21
  , 8.00
  , 21
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    612
  , 10477
  , 39
  , 14.40
  , 20
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    613
  , 10478
  , 10
  , 24.80
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    614
  , 10479
  , 38
  , 210.80
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    615
  , 10479
  , 53
  , 26.20
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    616
  , 10479
  , 59
  , 44.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    617
  , 10479
  , 64
  , 26.60
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    618
  , 10480
  , 47
  , 7.60
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    619
  , 10480
  , 59
  , 44.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    620
  , 10481
  , 49
  , 16.00
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    621
  , 10481
  , 60
  , 27.20
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    622
  , 10482
  , 40
  , 14.70
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    623
  , 10483
  , 34
  , 11.20
  , 35
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    624
  , 10483
  , 77
  , 10.40
  , 30
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    625
  , 10484
  , 21
  , 8.00
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    626
  , 10484
  , 40
  , 14.70
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    627
  , 10484
  , 51
  , 42.40
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    628
  , 10485
  , 2
  , 15.20
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    629
  , 10485
  , 3
  , 8.00
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    630
  , 10485
  , 55
  , 19.20
  , 30
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    631
  , 10485
  , 70
  , 12.00
  , 60
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    632
  , 10486
  , 11
  , 16.80
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    633
  , 10486
  , 51
  , 42.40
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    634
  , 10486
  , 74
  , 8.00
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    635
  , 10487
  , 19
  , 7.30
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    636
  , 10487
  , 26
  , 24.90
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    637
  , 10487
  , 54
  , 5.90
  , 24
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    638
  , 10488
  , 59
  , 44.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    639
  , 10488
  , 73
  , 12.00
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    640
  , 10489
  , 11
  , 16.80
  , 15
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    641
  , 10489
  , 16
  , 13.90
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    642
  , 10490
  , 59
  , 44.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    643
  , 10490
  , 68
  , 10.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    644
  , 10490
  , 75
  , 6.20
  , 36
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    645
  , 10491
  , 44
  , 15.50
  , 15
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    646
  , 10491
  , 77
  , 10.40
  , 7
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    647
  , 10492
  , 25
  , 11.20
  , 60
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    648
  , 10492
  , 42
  , 11.20
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    649
  , 10493
  , 65
  , 16.80
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    650
  , 10493
  , 66
  , 13.60
  , 10
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    651
  , 10493
  , 69
  , 28.80
  , 10
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    652
  , 10494
  , 56
  , 30.40
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    653
  , 10495
  , 23
  , 7.20
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    654
  , 10495
  , 41
  , 7.70
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    655
  , 10495
  , 77
  , 10.40
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    656
  , 10496
  , 31
  , 10.00
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    657
  , 10497
  , 56
  , 30.40
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    658
  , 10497
  , 72
  , 27.80
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    659
  , 10497
  , 77
  , 10.40
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    660
  , 10498
  , 24
  , 4.50
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    661
  , 10498
  , 40
  , 18.40
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    662
  , 10498
  , 42
  , 14.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    663
  , 10499
  , 28
  , 45.60
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    664
  , 10499
  , 49
  , 20.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    665
  , 10500
  , 15
  , 15.50
  , 12
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    666
  , 10500
  , 28
  , 45.60
  , 8
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    667
  , 10501
  , 54
  , 7.45
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    668
  , 10502
  , 45
  , 9.50
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    669
  , 10502
  , 53
  , 32.80
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    670
  , 10502
  , 67
  , 14.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    671
  , 10503
  , 14
  , 23.25
  , 70
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    672
  , 10503
  , 65
  , 21.05
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    673
  , 10504
  , 2
  , 19.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    674
  , 10504
  , 21
  , 10.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    675
  , 10504
  , 53
  , 32.80
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    676
  , 10504
  , 61
  , 28.50
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    677
  , 10505
  , 62
  , 49.30
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    678
  , 10506
  , 25
  , 14.00
  , 18
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    679
  , 10506
  , 70
  , 15.00
  , 14
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    680
  , 10507
  , 43
  , 46.00
  , 15
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    681
  , 10507
  , 48
  , 12.75
  , 15
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    682
  , 10508
  , 13
  , 6.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    683
  , 10508
  , 39
  , 18.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    684
  , 10509
  , 28
  , 45.60
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    685
  , 10510
  , 29
  , 123.79
  , 36
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    686
  , 10510
  , 75
  , 7.75
  , 36
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    687
  , 10511
  , 4
  , 22.00
  , 50
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    688
  , 10511
  , 7
  , 30.00
  , 50
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    689
  , 10511
  , 8
  , 40.00
  , 10
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    690
  , 10512
  , 24
  , 4.50
  , 10
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    691
  , 10512
  , 46
  , 12.00
  , 9
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    692
  , 10512
  , 47
  , 9.50
  , 6
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    693
  , 10512
  , 60
  , 34.00
  , 12
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    694
  , 10513
  , 21
  , 10.00
  , 40
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    695
  , 10513
  , 32
  , 32.00
  , 50
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    696
  , 10513
  , 61
  , 28.50
  , 15
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    697
  , 10514
  , 20
  , 81.00
  , 39
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    698
  , 10514
  , 28
  , 45.60
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    699
  , 10514
  , 56
  , 38.00
  , 70
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    700
  , 10514
  , 65
  , 21.05
  , 39
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    701
  , 10514
  , 75
  , 7.75
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    702
  , 10515
  , 9
  , 97.00
  , 16
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    703
  , 10515
  , 16
  , 17.45
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    704
  , 10515
  , 27
  , 43.90
  , 120
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    705
  , 10515
  , 33
  , 2.50
  , 16
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    706
  , 10515
  , 60
  , 34.00
  , 84
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    707
  , 10516
  , 18
  , 62.50
  , 25
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    708
  , 10516
  , 41
  , 9.65
  , 80
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    709
  , 10516
  , 42
  , 14.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    710
  , 10517
  , 52
  , 7.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    711
  , 10517
  , 59
  , 55.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    712
  , 10517
  , 70
  , 15.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    713
  , 10518
  , 24
  , 4.50
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    714
  , 10518
  , 38
  , 263.50
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    715
  , 10518
  , 44
  , 19.45
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    716
  , 10519
  , 10
  , 31.00
  , 16
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    717
  , 10519
  , 56
  , 38.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    718
  , 10519
  , 60
  , 34.00
  , 10
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    719
  , 10520
  , 24
  , 4.50
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    720
  , 10520
  , 53
  , 32.80
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    721
  , 10521
  , 35
  , 18.00
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    722
  , 10521
  , 41
  , 9.65
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    723
  , 10521
  , 68
  , 12.50
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    724
  , 10522
  , 1
  , 18.00
  , 40
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    725
  , 10522
  , 8
  , 40.00
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    726
  , 10522
  , 30
  , 25.89
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    727
  , 10522
  , 40
  , 18.40
  , 25
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    728
  , 10523
  , 17
  , 39.00
  , 25
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    729
  , 10523
  , 20
  , 81.00
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    730
  , 10523
  , 37
  , 26.00
  , 18
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    731
  , 10523
  , 41
  , 9.65
  , 6
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    732
  , 10524
  , 10
  , 31.00
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    733
  , 10524
  , 30
  , 25.89
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    734
  , 10524
  , 43
  , 46.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    735
  , 10524
  , 54
  , 7.45
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    736
  , 10525
  , 36
  , 19.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    737
  , 10525
  , 40
  , 18.40
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    738
  , 10526
  , 1
  , 18.00
  , 8
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    739
  , 10526
  , 13
  , 6.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    740
  , 10526
  , 56
  , 38.00
  , 30
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    741
  , 10527
  , 4
  , 22.00
  , 50
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    742
  , 10527
  , 36
  , 19.00
  , 30
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    743
  , 10528
  , 11
  , 21.00
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    744
  , 10528
  , 33
  , 2.50
  , 8
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    745
  , 10528
  , 72
  , 34.80
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    746
  , 10529
  , 55
  , 24.00
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    747
  , 10529
  , 68
  , 12.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    748
  , 10529
  , 69
  , 36.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    749
  , 10530
  , 17
  , 39.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    750
  , 10530
  , 43
  , 46.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    751
  , 10530
  , 61
  , 28.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    752
  , 10530
  , 76
  , 18.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    753
  , 10531
  , 59
  , 55.00
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    754
  , 10532
  , 30
  , 25.89
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    755
  , 10532
  , 66
  , 17.00
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    756
  , 10533
  , 4
  , 22.00
  , 50
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    757
  , 10533
  , 72
  , 34.80
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    758
  , 10533
  , 73
  , 15.00
  , 24
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    759
  , 10534
  , 30
  , 25.89
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    760
  , 10534
  , 40
  , 18.40
  , 10
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    761
  , 10534
  , 54
  , 7.45
  , 10
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    762
  , 10535
  , 11
  , 21.00
  , 50
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    763
  , 10535
  , 40
  , 18.40
  , 10
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    764
  , 10535
  , 57
  , 19.50
  , 5
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    765
  , 10535
  , 59
  , 55.00
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    766
  , 10536
  , 12
  , 38.00
  , 15
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    767
  , 10536
  , 31
  , 12.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    768
  , 10536
  , 33
  , 2.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    769
  , 10536
  , 60
  , 34.00
  , 35
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    770
  , 10537
  , 31
  , 12.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    771
  , 10537
  , 51
  , 53.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    772
  , 10537
  , 58
  , 13.25
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    773
  , 10537
  , 72
  , 34.80
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    774
  , 10537
  , 73
  , 15.00
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    775
  , 10538
  , 70
  , 15.00
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    776
  , 10538
  , 72
  , 34.80
  , 1
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    777
  , 10539
  , 13
  , 6.00
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    778
  , 10539
  , 21
  , 10.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    779
  , 10539
  , 33
  , 2.50
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    780
  , 10539
  , 49
  , 20.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    781
  , 10540
  , 3
  , 10.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    782
  , 10540
  , 26
  , 31.23
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    783
  , 10540
  , 38
  , 263.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    784
  , 10540
  , 68
  , 12.50
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    785
  , 10541
  , 24
  , 4.50
  , 35
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    786
  , 10541
  , 38
  , 263.50
  , 4
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    787
  , 10541
  , 65
  , 21.05
  , 36
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    788
  , 10541
  , 71
  , 21.50
  , 9
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    789
  , 10542
  , 11
  , 21.00
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    790
  , 10542
  , 54
  , 7.45
  , 24
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    791
  , 10543
  , 12
  , 38.00
  , 30
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    792
  , 10543
  , 23
  , 9.00
  , 70
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    793
  , 10544
  , 28
  , 45.60
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    794
  , 10544
  , 67
  , 14.00
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    795
  , 10545
  , 11
  , 21.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    796
  , 10546
  , 7
  , 30.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    797
  , 10546
  , 35
  , 18.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    798
  , 10546
  , 62
  , 49.30
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    799
  , 10547
  , 32
  , 32.00
  , 24
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    800
  , 10547
  , 36
  , 19.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    801
  , 10548
  , 34
  , 14.00
  , 10
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    802
  , 10548
  , 41
  , 9.65
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    803
  , 10549
  , 31
  , 12.50
  , 55
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    804
  , 10549
  , 45
  , 9.50
  , 100
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    805
  , 10549
  , 51
  , 53.00
  , 48
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    806
  , 10550
  , 17
  , 39.00
  , 8
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    807
  , 10550
  , 19
  , 9.20
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    808
  , 10550
  , 21
  , 10.00
  , 6
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    809
  , 10550
  , 61
  , 28.50
  , 10
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    810
  , 10551
  , 16
  , 17.45
  , 40
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    811
  , 10551
  , 35
  , 18.00
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    812
  , 10551
  , 44
  , 19.45
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    813
  , 10552
  , 69
  , 36.00
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    814
  , 10552
  , 75
  , 7.75
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    815
  , 10553
  , 11
  , 21.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    816
  , 10553
  , 16
  , 17.45
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    817
  , 10553
  , 22
  , 21.00
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    818
  , 10553
  , 31
  , 12.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    819
  , 10553
  , 35
  , 18.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    820
  , 10554
  , 16
  , 17.45
  , 30
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    821
  , 10554
  , 23
  , 9.00
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    822
  , 10554
  , 62
  , 49.30
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    823
  , 10554
  , 77
  , 13.00
  , 10
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    824
  , 10555
  , 14
  , 23.25
  , 30
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    825
  , 10555
  , 19
  , 9.20
  , 35
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    826
  , 10555
  , 24
  , 4.50
  , 18
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    827
  , 10555
  , 51
  , 53.00
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    828
  , 10555
  , 56
  , 38.00
  , 40
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    829
  , 10556
  , 72
  , 34.80
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    830
  , 10557
  , 64
  , 33.25
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    831
  , 10557
  , 75
  , 7.75
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    832
  , 10558
  , 47
  , 9.50
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    833
  , 10558
  , 51
  , 53.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    834
  , 10558
  , 52
  , 7.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    835
  , 10558
  , 53
  , 32.80
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    836
  , 10558
  , 73
  , 15.00
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    837
  , 10559
  , 41
  , 9.65
  , 12
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    838
  , 10559
  , 55
  , 24.00
  , 18
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    839
  , 10560
  , 30
  , 25.89
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    840
  , 10560
  , 62
  , 49.30
  , 15
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    841
  , 10561
  , 44
  , 19.45
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    842
  , 10561
  , 51
  , 53.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    843
  , 10562
  , 33
  , 2.50
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    844
  , 10562
  , 62
  , 49.30
  , 10
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    845
  , 10563
  , 36
  , 19.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    846
  , 10563
  , 52
  , 7.00
  , 70
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    847
  , 10564
  , 17
  , 39.00
  , 16
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    848
  , 10564
  , 31
  , 12.50
  , 6
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    849
  , 10564
  , 55
  , 24.00
  , 25
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    850
  , 10565
  , 24
  , 4.50
  , 25
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    851
  , 10565
  , 64
  , 33.25
  , 18
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    852
  , 10566
  , 11
  , 21.00
  , 35
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    853
  , 10566
  , 18
  , 62.50
  , 18
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    854
  , 10566
  , 76
  , 18.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    855
  , 10567
  , 31
  , 12.50
  , 60
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    856
  , 10567
  , 51
  , 53.00
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    857
  , 10567
  , 59
  , 55.00
  , 40
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    858
  , 10568
  , 10
  , 31.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    859
  , 10569
  , 31
  , 12.50
  , 35
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    860
  , 10569
  , 76
  , 18.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    861
  , 10570
  , 11
  , 21.00
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    862
  , 10570
  , 56
  , 38.00
  , 60
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    863
  , 10571
  , 14
  , 23.25
  , 11
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    864
  , 10571
  , 42
  , 14.00
  , 28
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    865
  , 10572
  , 16
  , 17.45
  , 12
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    866
  , 10572
  , 32
  , 32.00
  , 10
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    867
  , 10572
  , 40
  , 18.40
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    868
  , 10572
  , 75
  , 7.75
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    869
  , 10573
  , 17
  , 39.00
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    870
  , 10573
  , 34
  , 14.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    871
  , 10573
  , 53
  , 32.80
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    872
  , 10574
  , 33
  , 2.50
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    873
  , 10574
  , 40
  , 18.40
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    874
  , 10574
  , 62
  , 49.30
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    875
  , 10574
  , 64
  , 33.25
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    876
  , 10575
  , 59
  , 55.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    877
  , 10575
  , 63
  , 43.90
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    878
  , 10575
  , 72
  , 34.80
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    879
  , 10575
  , 76
  , 18.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    880
  , 10576
  , 1
  , 18.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    881
  , 10576
  , 31
  , 12.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    882
  , 10576
  , 44
  , 19.45
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    883
  , 10577
  , 39
  , 18.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    884
  , 10577
  , 75
  , 7.75
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    885
  , 10577
  , 77
  , 13.00
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    886
  , 10578
  , 35
  , 18.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    887
  , 10578
  , 57
  , 19.50
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    888
  , 10579
  , 15
  , 15.50
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    889
  , 10579
  , 75
  , 7.75
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    890
  , 10580
  , 14
  , 23.25
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    891
  , 10580
  , 41
  , 9.65
  , 9
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    892
  , 10580
  , 65
  , 21.05
  , 30
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    893
  , 10581
  , 75
  , 7.75
  , 50
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    894
  , 10582
  , 57
  , 19.50
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    895
  , 10582
  , 76
  , 18.00
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    896
  , 10583
  , 29
  , 123.79
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    897
  , 10583
  , 60
  , 34.00
  , 24
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    898
  , 10583
  , 69
  , 36.00
  , 10
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    899
  , 10584
  , 31
  , 12.50
  , 50
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    900
  , 10585
  , 47
  , 9.50
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    901
  , 10586
  , 52
  , 7.00
  , 4
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    902
  , 10587
  , 26
  , 31.23
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    903
  , 10587
  , 35
  , 18.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    904
  , 10587
  , 77
  , 13.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    905
  , 10588
  , 18
  , 62.50
  , 40
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    906
  , 10588
  , 42
  , 14.00
  , 100
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    907
  , 10589
  , 35
  , 18.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    908
  , 10590
  , 1
  , 18.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    909
  , 10590
  , 77
  , 13.00
  , 60
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    910
  , 10591
  , 3
  , 10.00
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    911
  , 10591
  , 7
  , 30.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    912
  , 10591
  , 54
  , 7.45
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    913
  , 10592
  , 15
  , 15.50
  , 25
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    914
  , 10592
  , 26
  , 31.23
  , 5
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    915
  , 10593
  , 20
  , 81.00
  , 21
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    916
  , 10593
  , 69
  , 36.00
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    917
  , 10593
  , 76
  , 18.00
  , 4
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    918
  , 10594
  , 52
  , 7.00
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    919
  , 10594
  , 58
  , 13.25
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    920
  , 10595
  , 35
  , 18.00
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    921
  , 10595
  , 61
  , 28.50
  , 120
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    922
  , 10595
  , 69
  , 36.00
  , 65
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    923
  , 10596
  , 56
  , 38.00
  , 5
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    924
  , 10596
  , 63
  , 43.90
  , 24
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    925
  , 10596
  , 75
  , 7.75
  , 30
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    926
  , 10597
  , 24
  , 4.50
  , 35
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    927
  , 10597
  , 57
  , 19.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    928
  , 10597
  , 65
  , 21.05
  , 12
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    929
  , 10598
  , 27
  , 43.90
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    930
  , 10598
  , 71
  , 21.50
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    931
  , 10599
  , 62
  , 49.30
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    932
  , 10600
  , 54
  , 7.45
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    933
  , 10600
  , 73
  , 15.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    934
  , 10601
  , 13
  , 6.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    935
  , 10601
  , 59
  , 55.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    936
  , 10602
  , 77
  , 13.00
  , 5
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    937
  , 10603
  , 22
  , 21.00
  , 48
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    938
  , 10603
  , 49
  , 20.00
  , 25
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    939
  , 10604
  , 48
  , 12.75
  , 6
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    940
  , 10604
  , 76
  , 18.00
  , 10
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    941
  , 10605
  , 16
  , 17.45
  , 30
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    942
  , 10605
  , 59
  , 55.00
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    943
  , 10605
  , 60
  , 34.00
  , 70
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    944
  , 10605
  , 71
  , 21.50
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    945
  , 10606
  , 4
  , 22.00
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    946
  , 10606
  , 55
  , 24.00
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    947
  , 10606
  , 62
  , 49.30
  , 10
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    948
  , 10607
  , 7
  , 30.00
  , 45
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    949
  , 10607
  , 17
  , 39.00
  , 100
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    950
  , 10607
  , 33
  , 2.50
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    951
  , 10607
  , 40
  , 18.40
  , 42
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    952
  , 10607
  , 72
  , 34.80
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    953
  , 10608
  , 56
  , 38.00
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    954
  , 10609
  , 1
  , 18.00
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    955
  , 10609
  , 10
  , 31.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    956
  , 10609
  , 21
  , 10.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    957
  , 10610
  , 36
  , 19.00
  , 21
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    958
  , 10611
  , 1
  , 18.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    959
  , 10611
  , 2
  , 19.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    960
  , 10611
  , 60
  , 34.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    961
  , 10612
  , 10
  , 31.00
  , 70
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    962
  , 10612
  , 36
  , 19.00
  , 55
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    963
  , 10612
  , 49
  , 20.00
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    964
  , 10612
  , 60
  , 34.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    965
  , 10612
  , 76
  , 18.00
  , 80
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    966
  , 10613
  , 13
  , 6.00
  , 8
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    967
  , 10613
  , 75
  , 7.75
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    968
  , 10614
  , 11
  , 21.00
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    969
  , 10614
  , 21
  , 10.00
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    970
  , 10614
  , 39
  , 18.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    971
  , 10615
  , 55
  , 24.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    972
  , 10616
  , 38
  , 263.50
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    973
  , 10616
  , 56
  , 38.00
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    974
  , 10616
  , 70
  , 15.00
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    975
  , 10616
  , 71
  , 21.50
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    976
  , 10617
  , 59
  , 55.00
  , 30
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    977
  , 10618
  , 6
  , 25.00
  , 70
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    978
  , 10618
  , 56
  , 38.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    979
  , 10618
  , 68
  , 12.50
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    980
  , 10619
  , 21
  , 10.00
  , 42
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    981
  , 10619
  , 22
  , 21.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    982
  , 10620
  , 24
  , 4.50
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    983
  , 10620
  , 52
  , 7.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    984
  , 10621
  , 19
  , 9.20
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    985
  , 10621
  , 23
  , 9.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    986
  , 10621
  , 70
  , 15.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    987
  , 10621
  , 71
  , 21.50
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    988
  , 10622
  , 2
  , 19.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    989
  , 10622
  , 68
  , 12.50
  , 18
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    990
  , 10623
  , 14
  , 23.25
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    991
  , 10623
  , 19
  , 9.20
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    992
  , 10623
  , 21
  , 10.00
  , 25
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    993
  , 10623
  , 24
  , 4.50
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    994
  , 10623
  , 35
  , 18.00
  , 30
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    995
  , 10624
  , 28
  , 45.60
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    996
  , 10624
  , 29
  , 123.79
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    997
  , 10624
  , 44
  , 19.45
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    998
  , 10625
  , 14
  , 23.25
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    999
  , 10625
  , 42
  , 14.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1000
  , 10625
  , 60
  , 34.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1001
  , 10626
  , 53
  , 32.80
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1002
  , 10626
  , 60
  , 34.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1003
  , 10626
  , 71
  , 21.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1004
  , 10627
  , 62
  , 49.30
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1005
  , 10627
  , 73
  , 15.00
  , 35
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1006
  , 10628
  , 1
  , 18.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1007
  , 10629
  , 29
  , 123.79
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1008
  , 10629
  , 64
  , 33.25
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1009
  , 10630
  , 55
  , 24.00
  , 12
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1010
  , 10630
  , 76
  , 18.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1011
  , 10631
  , 75
  , 7.75
  , 8
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1012
  , 10632
  , 2
  , 19.00
  , 30
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1013
  , 10632
  , 33
  , 2.50
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1014
  , 10633
  , 12
  , 38.00
  , 36
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1015
  , 10633
  , 13
  , 6.00
  , 13
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1016
  , 10633
  , 26
  , 31.23
  , 35
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1017
  , 10633
  , 62
  , 49.30
  , 80
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1018
  , 10634
  , 7
  , 30.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1019
  , 10634
  , 18
  , 62.50
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1020
  , 10634
  , 51
  , 53.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1021
  , 10634
  , 75
  , 7.75
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1022
  , 10635
  , 4
  , 22.00
  , 10
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1023
  , 10635
  , 5
  , 21.35
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1024
  , 10635
  , 22
  , 21.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1025
  , 10636
  , 4
  , 22.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1026
  , 10636
  , 58
  , 13.25
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1027
  , 10637
  , 11
  , 21.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1028
  , 10637
  , 50
  , 16.25
  , 25
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1029
  , 10637
  , 56
  , 38.00
  , 60
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1030
  , 10638
  , 45
  , 9.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1031
  , 10638
  , 65
  , 21.05
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1032
  , 10638
  , 72
  , 34.80
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1033
  , 10639
  , 18
  , 62.50
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1034
  , 10640
  , 69
  , 36.00
  , 20
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1035
  , 10640
  , 70
  , 15.00
  , 15
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1036
  , 10641
  , 2
  , 19.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1037
  , 10641
  , 40
  , 18.40
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1038
  , 10642
  , 21
  , 10.00
  , 30
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1039
  , 10642
  , 61
  , 28.50
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1040
  , 10643
  , 28
  , 45.60
  , 15
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1041
  , 10643
  , 39
  , 18.00
  , 21
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1042
  , 10643
  , 46
  , 12.00
  , 2
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1043
  , 10644
  , 18
  , 62.50
  , 4
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1044
  , 10644
  , 43
  , 46.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1045
  , 10644
  , 46
  , 12.00
  , 21
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1046
  , 10645
  , 18
  , 62.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1047
  , 10645
  , 36
  , 19.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1048
  , 10646
  , 1
  , 18.00
  , 15
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1049
  , 10646
  , 10
  , 31.00
  , 18
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1050
  , 10646
  , 71
  , 21.50
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1051
  , 10646
  , 77
  , 13.00
  , 35
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1052
  , 10647
  , 19
  , 9.20
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1053
  , 10647
  , 39
  , 18.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1054
  , 10648
  , 22
  , 21.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1055
  , 10648
  , 24
  , 4.50
  , 15
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1056
  , 10649
  , 28
  , 45.60
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1057
  , 10649
  , 72
  , 34.80
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1058
  , 10650
  , 30
  , 25.89
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1059
  , 10650
  , 53
  , 32.80
  , 25
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1060
  , 10650
  , 54
  , 7.45
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1061
  , 10651
  , 19
  , 9.20
  , 12
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1062
  , 10651
  , 22
  , 21.00
  , 20
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1063
  , 10652
  , 30
  , 25.89
  , 2
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1064
  , 10652
  , 42
  , 14.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1065
  , 10653
  , 16
  , 17.45
  , 30
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1066
  , 10653
  , 60
  , 34.00
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1067
  , 10654
  , 4
  , 22.00
  , 12
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1068
  , 10654
  , 39
  , 18.00
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1069
  , 10654
  , 54
  , 7.45
  , 6
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1070
  , 10655
  , 41
  , 9.65
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1071
  , 10656
  , 14
  , 23.25
  , 3
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1072
  , 10656
  , 44
  , 19.45
  , 28
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1073
  , 10656
  , 47
  , 9.50
  , 6
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1074
  , 10657
  , 15
  , 15.50
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1075
  , 10657
  , 41
  , 9.65
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1076
  , 10657
  , 46
  , 12.00
  , 45
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1077
  , 10657
  , 47
  , 9.50
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1078
  , 10657
  , 56
  , 38.00
  , 45
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1079
  , 10657
  , 60
  , 34.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1080
  , 10658
  , 21
  , 10.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1081
  , 10658
  , 40
  , 18.40
  , 70
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1082
  , 10658
  , 60
  , 34.00
  , 55
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1083
  , 10658
  , 77
  , 13.00
  , 70
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1084
  , 10659
  , 31
  , 12.50
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1085
  , 10659
  , 40
  , 18.40
  , 24
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1086
  , 10659
  , 70
  , 15.00
  , 40
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1087
  , 10660
  , 20
  , 81.00
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1088
  , 10661
  , 39
  , 18.00
  , 3
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1089
  , 10661
  , 58
  , 13.25
  , 49
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1090
  , 10662
  , 68
  , 12.50
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1091
  , 10663
  , 40
  , 18.40
  , 30
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1092
  , 10663
  , 42
  , 14.00
  , 30
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1093
  , 10663
  , 51
  , 53.00
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1094
  , 10664
  , 10
  , 31.00
  , 24
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1095
  , 10664
  , 56
  , 38.00
  , 12
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1096
  , 10664
  , 65
  , 21.05
  , 15
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1097
  , 10665
  , 51
  , 53.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1098
  , 10665
  , 59
  , 55.00
  , 1
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1099
  , 10665
  , 76
  , 18.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1100
  , 10666
  , 29
  , 123.79
  , 36
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1101
  , 10666
  , 65
  , 21.05
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1102
  , 10667
  , 69
  , 36.00
  , 45
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1103
  , 10667
  , 71
  , 21.50
  , 14
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1104
  , 10668
  , 31
  , 12.50
  , 8
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1105
  , 10668
  , 55
  , 24.00
  , 4
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1106
  , 10668
  , 64
  , 33.25
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1107
  , 10669
  , 36
  , 19.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1108
  , 10670
  , 23
  , 9.00
  , 32
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1109
  , 10670
  , 46
  , 12.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1110
  , 10670
  , 67
  , 14.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1111
  , 10670
  , 73
  , 15.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1112
  , 10670
  , 75
  , 7.75
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1113
  , 10671
  , 16
  , 17.45
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1114
  , 10671
  , 62
  , 49.30
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1115
  , 10671
  , 65
  , 21.05
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1116
  , 10672
  , 38
  , 263.50
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1117
  , 10672
  , 71
  , 21.50
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1118
  , 10673
  , 16
  , 17.45
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1119
  , 10673
  , 42
  , 14.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1120
  , 10673
  , 43
  , 46.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1121
  , 10674
  , 23
  , 9.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1122
  , 10675
  , 14
  , 23.25
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1123
  , 10675
  , 53
  , 32.80
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1124
  , 10675
  , 58
  , 13.25
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1125
  , 10676
  , 10
  , 31.00
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1126
  , 10676
  , 19
  , 9.20
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1127
  , 10676
  , 44
  , 19.45
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1128
  , 10677
  , 26
  , 31.23
  , 30
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1129
  , 10677
  , 33
  , 2.50
  , 8
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1130
  , 10678
  , 12
  , 38.00
  , 100
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1131
  , 10678
  , 33
  , 2.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1132
  , 10678
  , 41
  , 9.65
  , 120
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1133
  , 10678
  , 54
  , 7.45
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1134
  , 10679
  , 59
  , 55.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1135
  , 10680
  , 16
  , 17.45
  , 50
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1136
  , 10680
  , 31
  , 12.50
  , 20
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1137
  , 10680
  , 42
  , 14.00
  , 40
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1138
  , 10681
  , 19
  , 9.20
  , 30
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1139
  , 10681
  , 21
  , 10.00
  , 12
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1140
  , 10681
  , 64
  , 33.25
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1141
  , 10682
  , 33
  , 2.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1142
  , 10682
  , 66
  , 17.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1143
  , 10682
  , 75
  , 7.75
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1144
  , 10683
  , 52
  , 7.00
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1145
  , 10684
  , 40
  , 18.40
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1146
  , 10684
  , 47
  , 9.50
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1147
  , 10684
  , 60
  , 34.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1148
  , 10685
  , 10
  , 31.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1149
  , 10685
  , 41
  , 9.65
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1150
  , 10685
  , 47
  , 9.50
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1151
  , 10686
  , 17
  , 39.00
  , 30
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1152
  , 10686
  , 26
  , 31.23
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1153
  , 10687
  , 9
  , 97.00
  , 50
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1154
  , 10687
  , 29
  , 123.79
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1155
  , 10687
  , 36
  , 19.00
  , 6
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1156
  , 10688
  , 10
  , 31.00
  , 18
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1157
  , 10688
  , 28
  , 45.60
  , 60
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1158
  , 10688
  , 34
  , 14.00
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1159
  , 10689
  , 1
  , 18.00
  , 35
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1160
  , 10690
  , 56
  , 38.00
  , 20
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1161
  , 10690
  , 77
  , 13.00
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1162
  , 10691
  , 1
  , 18.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1163
  , 10691
  , 29
  , 123.79
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1164
  , 10691
  , 43
  , 46.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1165
  , 10691
  , 44
  , 19.45
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1166
  , 10691
  , 62
  , 49.30
  , 48
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1167
  , 10692
  , 63
  , 43.90
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1168
  , 10693
  , 9
  , 97.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1169
  , 10693
  , 54
  , 7.45
  , 60
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1170
  , 10693
  , 69
  , 36.00
  , 30
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1171
  , 10693
  , 73
  , 15.00
  , 15
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1172
  , 10694
  , 7
  , 30.00
  , 90
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1173
  , 10694
  , 59
  , 55.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1174
  , 10694
  , 70
  , 15.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1175
  , 10695
  , 8
  , 40.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1176
  , 10695
  , 12
  , 38.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1177
  , 10695
  , 24
  , 4.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1178
  , 10696
  , 17
  , 39.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1179
  , 10696
  , 46
  , 12.00
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1180
  , 10697
  , 19
  , 9.20
  , 7
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1181
  , 10697
  , 35
  , 18.00
  , 9
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1182
  , 10697
  , 58
  , 13.25
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1183
  , 10697
  , 70
  , 15.00
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1184
  , 10698
  , 11
  , 21.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1185
  , 10698
  , 17
  , 39.00
  , 8
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1186
  , 10698
  , 29
  , 123.79
  , 12
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1187
  , 10698
  , 65
  , 21.05
  , 65
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1188
  , 10698
  , 70
  , 15.00
  , 8
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1189
  , 10699
  , 47
  , 9.50
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1190
  , 10700
  , 1
  , 18.00
  , 5
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1191
  , 10700
  , 34
  , 14.00
  , 12
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1192
  , 10700
  , 68
  , 12.50
  , 40
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1193
  , 10700
  , 71
  , 21.50
  , 60
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1194
  , 10701
  , 59
  , 55.00
  , 42
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1195
  , 10701
  , 71
  , 21.50
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1196
  , 10701
  , 76
  , 18.00
  , 35
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1197
  , 10702
  , 3
  , 10.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1198
  , 10702
  , 76
  , 18.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1199
  , 10703
  , 2
  , 19.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1200
  , 10703
  , 59
  , 55.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1201
  , 10703
  , 73
  , 15.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1202
  , 10704
  , 4
  , 22.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1203
  , 10704
  , 24
  , 4.50
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1204
  , 10704
  , 48
  , 12.75
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1205
  , 10705
  , 31
  , 12.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1206
  , 10705
  , 32
  , 32.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1207
  , 10706
  , 16
  , 17.45
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1208
  , 10706
  , 43
  , 46.00
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1209
  , 10706
  , 59
  , 55.00
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1210
  , 10707
  , 55
  , 24.00
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1211
  , 10707
  , 57
  , 19.50
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1212
  , 10707
  , 70
  , 15.00
  , 28
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1213
  , 10708
  , 5
  , 21.35
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1214
  , 10708
  , 36
  , 19.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1215
  , 10709
  , 8
  , 40.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1216
  , 10709
  , 51
  , 53.00
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1217
  , 10709
  , 60
  , 34.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1218
  , 10710
  , 19
  , 9.20
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1219
  , 10710
  , 47
  , 9.50
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1220
  , 10711
  , 19
  , 9.20
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1221
  , 10711
  , 41
  , 9.65
  , 42
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1222
  , 10711
  , 53
  , 32.80
  , 120
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1223
  , 10712
  , 53
  , 32.80
  , 3
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1224
  , 10712
  , 56
  , 38.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1225
  , 10713
  , 10
  , 31.00
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1226
  , 10713
  , 26
  , 31.23
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1227
  , 10713
  , 45
  , 9.50
  , 110
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1228
  , 10713
  , 46
  , 12.00
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1229
  , 10714
  , 2
  , 19.00
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1230
  , 10714
  , 17
  , 39.00
  , 27
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1231
  , 10714
  , 47
  , 9.50
  , 50
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1232
  , 10714
  , 56
  , 38.00
  , 18
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1233
  , 10714
  , 58
  , 13.25
  , 12
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1234
  , 10715
  , 10
  , 31.00
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1235
  , 10715
  , 71
  , 21.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1236
  , 10716
  , 21
  , 10.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1237
  , 10716
  , 51
  , 53.00
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1238
  , 10716
  , 61
  , 28.50
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1239
  , 10717
  , 21
  , 10.00
  , 32
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1240
  , 10717
  , 54
  , 7.45
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1241
  , 10717
  , 69
  , 36.00
  , 25
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1242
  , 10718
  , 12
  , 38.00
  , 36
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1243
  , 10718
  , 16
  , 17.45
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1244
  , 10718
  , 36
  , 19.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1245
  , 10718
  , 62
  , 49.30
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1246
  , 10719
  , 18
  , 62.50
  , 12
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1247
  , 10719
  , 30
  , 25.89
  , 3
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1248
  , 10719
  , 54
  , 7.45
  , 40
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1249
  , 10720
  , 35
  , 18.00
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1250
  , 10720
  , 71
  , 21.50
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1251
  , 10721
  , 44
  , 19.45
  , 50
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1252
  , 10722
  , 2
  , 19.00
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1253
  , 10722
  , 31
  , 12.50
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1254
  , 10722
  , 68
  , 12.50
  , 45
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1255
  , 10722
  , 75
  , 7.75
  , 42
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1256
  , 10723
  , 26
  , 31.23
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1257
  , 10724
  , 10
  , 31.00
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1258
  , 10724
  , 61
  , 28.50
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1259
  , 10725
  , 41
  , 9.65
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1260
  , 10725
  , 52
  , 7.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1261
  , 10725
  , 55
  , 24.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1262
  , 10726
  , 4
  , 22.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1263
  , 10726
  , 11
  , 21.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1264
  , 10727
  , 17
  , 39.00
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1265
  , 10727
  , 56
  , 38.00
  , 10
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1266
  , 10727
  , 59
  , 55.00
  , 10
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1267
  , 10728
  , 30
  , 25.89
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1268
  , 10728
  , 40
  , 18.40
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1269
  , 10728
  , 55
  , 24.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1270
  , 10728
  , 60
  , 34.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1271
  , 10729
  , 1
  , 18.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1272
  , 10729
  , 21
  , 10.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1273
  , 10729
  , 50
  , 16.25
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1274
  , 10730
  , 16
  , 17.45
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1275
  , 10730
  , 31
  , 12.50
  , 3
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1276
  , 10730
  , 65
  , 21.05
  , 10
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1277
  , 10731
  , 21
  , 10.00
  , 40
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1278
  , 10731
  , 51
  , 53.00
  , 30
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1279
  , 10732
  , 76
  , 18.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1280
  , 10733
  , 14
  , 23.25
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1281
  , 10733
  , 28
  , 45.60
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1282
  , 10733
  , 52
  , 7.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1283
  , 10734
  , 6
  , 25.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1284
  , 10734
  , 30
  , 25.89
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1285
  , 10734
  , 76
  , 18.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1286
  , 10735
  , 61
  , 28.50
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1287
  , 10735
  , 77
  , 13.00
  , 2
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1288
  , 10736
  , 65
  , 21.05
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1289
  , 10736
  , 75
  , 7.75
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1290
  , 10737
  , 13
  , 6.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1291
  , 10737
  , 41
  , 9.65
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1292
  , 10738
  , 16
  , 17.45
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1293
  , 10739
  , 36
  , 19.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1294
  , 10739
  , 52
  , 7.00
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1295
  , 10740
  , 28
  , 45.60
  , 5
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1296
  , 10740
  , 35
  , 18.00
  , 35
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1297
  , 10740
  , 45
  , 9.50
  , 40
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1298
  , 10740
  , 56
  , 38.00
  , 14
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1299
  , 10741
  , 2
  , 19.00
  , 15
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1300
  , 10742
  , 3
  , 10.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1301
  , 10742
  , 60
  , 34.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1302
  , 10742
  , 72
  , 34.80
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1303
  , 10743
  , 46
  , 12.00
  , 28
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1304
  , 10744
  , 40
  , 18.40
  , 50
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1305
  , 10745
  , 18
  , 62.50
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1306
  , 10745
  , 44
  , 19.45
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1307
  , 10745
  , 59
  , 55.00
  , 45
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1308
  , 10745
  , 72
  , 34.80
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1309
  , 10746
  , 13
  , 6.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1310
  , 10746
  , 42
  , 14.00
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1311
  , 10746
  , 62
  , 49.30
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1312
  , 10746
  , 69
  , 36.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1313
  , 10747
  , 31
  , 12.50
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1314
  , 10747
  , 41
  , 9.65
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1315
  , 10747
  , 63
  , 43.90
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1316
  , 10747
  , 69
  , 36.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1317
  , 10748
  , 23
  , 9.00
  , 44
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1318
  , 10748
  , 40
  , 18.40
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1319
  , 10748
  , 56
  , 38.00
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1320
  , 10749
  , 56
  , 38.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1321
  , 10749
  , 59
  , 55.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1322
  , 10749
  , 76
  , 18.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1323
  , 10750
  , 14
  , 23.25
  , 5
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1324
  , 10750
  , 45
  , 9.50
  , 40
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1325
  , 10750
  , 59
  , 55.00
  , 25
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1326
  , 10751
  , 26
  , 31.23
  , 12
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1327
  , 10751
  , 30
  , 25.89
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1328
  , 10751
  , 50
  , 16.25
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1329
  , 10751
  , 73
  , 15.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1330
  , 10752
  , 1
  , 18.00
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1331
  , 10752
  , 69
  , 36.00
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1332
  , 10753
  , 45
  , 9.50
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1333
  , 10753
  , 74
  , 10.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1334
  , 10754
  , 40
  , 18.40
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1335
  , 10755
  , 47
  , 9.50
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1336
  , 10755
  , 56
  , 38.00
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1337
  , 10755
  , 57
  , 19.50
  , 14
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1338
  , 10755
  , 69
  , 36.00
  , 25
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1339
  , 10756
  , 18
  , 62.50
  , 21
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1340
  , 10756
  , 36
  , 19.00
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1341
  , 10756
  , 68
  , 12.50
  , 6
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1342
  , 10756
  , 69
  , 36.00
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1343
  , 10757
  , 34
  , 14.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1344
  , 10757
  , 59
  , 55.00
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1345
  , 10757
  , 62
  , 49.30
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1346
  , 10757
  , 64
  , 33.25
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1347
  , 10758
  , 26
  , 31.23
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1348
  , 10758
  , 52
  , 7.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1349
  , 10758
  , 70
  , 15.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1350
  , 10759
  , 32
  , 32.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1351
  , 10760
  , 25
  , 14.00
  , 12
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1352
  , 10760
  , 27
  , 43.90
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1353
  , 10760
  , 43
  , 46.00
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1354
  , 10761
  , 25
  , 14.00
  , 35
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1355
  , 10761
  , 75
  , 7.75
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1356
  , 10762
  , 39
  , 18.00
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1357
  , 10762
  , 47
  , 9.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1358
  , 10762
  , 51
  , 53.00
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1359
  , 10762
  , 56
  , 38.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1360
  , 10763
  , 21
  , 10.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1361
  , 10763
  , 22
  , 21.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1362
  , 10763
  , 24
  , 4.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1363
  , 10764
  , 3
  , 10.00
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1364
  , 10764
  , 39
  , 18.00
  , 130
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1365
  , 10765
  , 65
  , 21.05
  , 80
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1366
  , 10766
  , 2
  , 19.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1367
  , 10766
  , 7
  , 30.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1368
  , 10766
  , 68
  , 12.50
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1369
  , 10767
  , 42
  , 14.00
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1370
  , 10768
  , 22
  , 21.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1371
  , 10768
  , 31
  , 12.50
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1372
  , 10768
  , 60
  , 34.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1373
  , 10768
  , 71
  , 21.50
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1374
  , 10769
  , 41
  , 9.65
  , 30
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1375
  , 10769
  , 52
  , 7.00
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1376
  , 10769
  , 61
  , 28.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1377
  , 10769
  , 62
  , 49.30
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1378
  , 10770
  , 11
  , 21.00
  , 15
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1379
  , 10771
  , 71
  , 21.50
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1380
  , 10772
  , 29
  , 123.79
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1381
  , 10772
  , 59
  , 55.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1382
  , 10773
  , 17
  , 39.00
  , 33
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1383
  , 10773
  , 31
  , 12.50
  , 70
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1384
  , 10773
  , 75
  , 7.75
  , 7
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1385
  , 10774
  , 31
  , 12.50
  , 2
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1386
  , 10774
  , 66
  , 17.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1387
  , 10775
  , 10
  , 31.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1388
  , 10775
  , 67
  , 14.00
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1389
  , 10776
  , 31
  , 12.50
  , 16
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1390
  , 10776
  , 42
  , 14.00
  , 12
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1391
  , 10776
  , 45
  , 9.50
  , 27
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1392
  , 10776
  , 51
  , 53.00
  , 120
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1393
  , 10777
  , 42
  , 14.00
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1394
  , 10778
  , 41
  , 9.65
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1395
  , 10779
  , 16
  , 17.45
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1396
  , 10779
  , 62
  , 49.30
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1397
  , 10780
  , 70
  , 15.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1398
  , 10780
  , 77
  , 13.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1399
  , 10781
  , 54
  , 7.45
  , 3
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1400
  , 10781
  , 56
  , 38.00
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1401
  , 10781
  , 74
  , 10.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1402
  , 10782
  , 31
  , 12.50
  , 1
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1403
  , 10783
  , 31
  , 12.50
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1404
  , 10783
  , 38
  , 263.50
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1405
  , 10784
  , 36
  , 19.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1406
  , 10784
  , 39
  , 18.00
  , 2
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1407
  , 10784
  , 72
  , 34.80
  , 30
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1408
  , 10785
  , 10
  , 31.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1409
  , 10785
  , 75
  , 7.75
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1410
  , 10786
  , 8
  , 40.00
  , 30
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1411
  , 10786
  , 30
  , 25.89
  , 15
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1412
  , 10786
  , 75
  , 7.75
  , 42
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1413
  , 10787
  , 2
  , 19.00
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1414
  , 10787
  , 29
  , 123.79
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1415
  , 10788
  , 19
  , 9.20
  , 50
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1416
  , 10788
  , 75
  , 7.75
  , 40
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1417
  , 10789
  , 18
  , 62.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1418
  , 10789
  , 35
  , 18.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1419
  , 10789
  , 63
  , 43.90
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1420
  , 10789
  , 68
  , 12.50
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1421
  , 10790
  , 7
  , 30.00
  , 3
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1422
  , 10790
  , 56
  , 38.00
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1423
  , 10791
  , 29
  , 123.79
  , 14
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1424
  , 10791
  , 41
  , 9.65
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1425
  , 10792
  , 2
  , 19.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1426
  , 10792
  , 54
  , 7.45
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1427
  , 10792
  , 68
  , 12.50
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1428
  , 10793
  , 41
  , 9.65
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1429
  , 10793
  , 52
  , 7.00
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1430
  , 10794
  , 14
  , 23.25
  , 15
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1431
  , 10794
  , 54
  , 7.45
  , 6
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1432
  , 10795
  , 16
  , 17.45
  , 65
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1433
  , 10795
  , 17
  , 39.00
  , 35
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1434
  , 10796
  , 26
  , 31.23
  , 21
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1435
  , 10796
  , 44
  , 19.45
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1436
  , 10796
  , 64
  , 33.25
  , 35
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1437
  , 10796
  , 69
  , 36.00
  , 24
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1438
  , 10797
  , 11
  , 21.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1439
  , 10798
  , 62
  , 49.30
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1440
  , 10798
  , 72
  , 34.80
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1441
  , 10799
  , 13
  , 6.00
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1442
  , 10799
  , 24
  , 4.50
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1443
  , 10799
  , 59
  , 55.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1444
  , 10800
  , 11
  , 21.00
  , 50
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1445
  , 10800
  , 51
  , 53.00
  , 10
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1446
  , 10800
  , 54
  , 7.45
  , 7
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1447
  , 10801
  , 17
  , 39.00
  , 40
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1448
  , 10801
  , 29
  , 123.79
  , 20
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1449
  , 10802
  , 30
  , 25.89
  , 25
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1450
  , 10802
  , 51
  , 53.00
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1451
  , 10802
  , 55
  , 24.00
  , 60
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1452
  , 10802
  , 62
  , 49.30
  , 5
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1453
  , 10803
  , 19
  , 9.20
  , 24
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1454
  , 10803
  , 25
  , 14.00
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1455
  , 10803
  , 59
  , 55.00
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1456
  , 10804
  , 10
  , 31.00
  , 36
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1457
  , 10804
  , 28
  , 45.60
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1458
  , 10804
  , 49
  , 20.00
  , 4
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1459
  , 10805
  , 34
  , 14.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1460
  , 10805
  , 38
  , 263.50
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1461
  , 10806
  , 2
  , 19.00
  , 20
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1462
  , 10806
  , 65
  , 21.05
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1463
  , 10806
  , 74
  , 10.00
  , 15
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1464
  , 10807
  , 40
  , 18.40
  , 1
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1465
  , 10808
  , 56
  , 38.00
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1466
  , 10808
  , 76
  , 18.00
  , 50
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1467
  , 10809
  , 52
  , 7.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1468
  , 10810
  , 13
  , 6.00
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1469
  , 10810
  , 25
  , 14.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1470
  , 10810
  , 70
  , 15.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1471
  , 10811
  , 19
  , 9.20
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1472
  , 10811
  , 23
  , 9.00
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1473
  , 10811
  , 40
  , 18.40
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1474
  , 10812
  , 31
  , 12.50
  , 16
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1475
  , 10812
  , 72
  , 34.80
  , 40
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1476
  , 10812
  , 77
  , 13.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1477
  , 10813
  , 2
  , 19.00
  , 12
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1478
  , 10813
  , 46
  , 12.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1479
  , 10814
  , 41
  , 9.65
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1480
  , 10814
  , 43
  , 46.00
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1481
  , 10814
  , 48
  , 12.75
  , 8
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1482
  , 10814
  , 61
  , 28.50
  , 30
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1483
  , 10815
  , 33
  , 2.50
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1484
  , 10816
  , 38
  , 263.50
  , 30
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1485
  , 10816
  , 62
  , 49.30
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1486
  , 10817
  , 26
  , 31.23
  , 40
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1487
  , 10817
  , 38
  , 263.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1488
  , 10817
  , 40
  , 18.40
  , 60
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1489
  , 10817
  , 62
  , 49.30
  , 25
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1490
  , 10818
  , 32
  , 32.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1491
  , 10818
  , 41
  , 9.65
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1492
  , 10819
  , 43
  , 46.00
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1493
  , 10819
  , 75
  , 7.75
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1494
  , 10820
  , 56
  , 38.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1495
  , 10821
  , 35
  , 18.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1496
  , 10821
  , 51
  , 53.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1497
  , 10822
  , 62
  , 49.30
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1498
  , 10822
  , 70
  , 15.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1499
  , 10823
  , 11
  , 21.00
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1500
  , 10823
  , 57
  , 19.50
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1501
  , 10823
  , 59
  , 55.00
  , 40
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1502
  , 10823
  , 77
  , 13.00
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1503
  , 10824
  , 41
  , 9.65
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1504
  , 10824
  , 70
  , 15.00
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1505
  , 10825
  , 26
  , 31.23
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1506
  , 10825
  , 53
  , 32.80
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1507
  , 10826
  , 31
  , 12.50
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1508
  , 10826
  , 57
  , 19.50
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1509
  , 10827
  , 10
  , 31.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1510
  , 10827
  , 39
  , 18.00
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1511
  , 10828
  , 20
  , 81.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1512
  , 10828
  , 38
  , 263.50
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1513
  , 10829
  , 2
  , 19.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1514
  , 10829
  , 8
  , 40.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1515
  , 10829
  , 13
  , 6.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1516
  , 10829
  , 60
  , 34.00
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1517
  , 10830
  , 6
  , 25.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1518
  , 10830
  , 39
  , 18.00
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1519
  , 10830
  , 60
  , 34.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1520
  , 10830
  , 68
  , 12.50
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1521
  , 10831
  , 19
  , 9.20
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1522
  , 10831
  , 35
  , 18.00
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1523
  , 10831
  , 38
  , 263.50
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1524
  , 10831
  , 43
  , 46.00
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1525
  , 10832
  , 13
  , 6.00
  , 3
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1526
  , 10832
  , 25
  , 14.00
  , 10
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1527
  , 10832
  , 44
  , 19.45
  , 16
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1528
  , 10832
  , 64
  , 33.25
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1529
  , 10833
  , 7
  , 30.00
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1530
  , 10833
  , 31
  , 12.50
  , 9
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1531
  , 10833
  , 53
  , 32.80
  , 9
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1532
  , 10834
  , 29
  , 123.79
  , 8
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1533
  , 10834
  , 30
  , 25.89
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1534
  , 10835
  , 59
  , 55.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1535
  , 10835
  , 77
  , 13.00
  , 2
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1536
  , 10836
  , 22
  , 21.00
  , 52
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1537
  , 10836
  , 35
  , 18.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1538
  , 10836
  , 57
  , 19.50
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1539
  , 10836
  , 60
  , 34.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1540
  , 10836
  , 64
  , 33.25
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1541
  , 10837
  , 13
  , 6.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1542
  , 10837
  , 40
  , 18.40
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1543
  , 10837
  , 47
  , 9.50
  , 40
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1544
  , 10837
  , 76
  , 18.00
  , 21
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1545
  , 10838
  , 1
  , 18.00
  , 4
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1546
  , 10838
  , 18
  , 62.50
  , 25
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1547
  , 10838
  , 36
  , 19.00
  , 50
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1548
  , 10839
  , 58
  , 13.25
  , 30
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1549
  , 10839
  , 72
  , 34.80
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1550
  , 10840
  , 25
  , 14.00
  , 6
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1551
  , 10840
  , 39
  , 18.00
  , 10
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1552
  , 10841
  , 10
  , 31.00
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1553
  , 10841
  , 56
  , 38.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1554
  , 10841
  , 59
  , 55.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1555
  , 10841
  , 77
  , 13.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1556
  , 10842
  , 11
  , 21.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1557
  , 10842
  , 43
  , 46.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1558
  , 10842
  , 68
  , 12.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1559
  , 10842
  , 70
  , 15.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1560
  , 10843
  , 51
  , 53.00
  , 4
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1561
  , 10844
  , 22
  , 21.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1562
  , 10845
  , 23
  , 9.00
  , 70
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1563
  , 10845
  , 35
  , 18.00
  , 25
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1564
  , 10845
  , 42
  , 14.00
  , 42
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1565
  , 10845
  , 58
  , 13.25
  , 60
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1566
  , 10845
  , 64
  , 33.25
  , 48
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1567
  , 10846
  , 4
  , 22.00
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1568
  , 10846
  , 70
  , 15.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1569
  , 10846
  , 74
  , 10.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1570
  , 10847
  , 1
  , 18.00
  , 80
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1571
  , 10847
  , 19
  , 9.20
  , 12
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1572
  , 10847
  , 37
  , 26.00
  , 60
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1573
  , 10847
  , 45
  , 9.50
  , 36
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1574
  , 10847
  , 60
  , 34.00
  , 45
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1575
  , 10847
  , 71
  , 21.50
  , 55
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1576
  , 10848
  , 5
  , 21.35
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1577
  , 10848
  , 9
  , 97.00
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1578
  , 10849
  , 3
  , 10.00
  , 49
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1579
  , 10849
  , 26
  , 31.23
  , 18
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1580
  , 10850
  , 25
  , 14.00
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1581
  , 10850
  , 33
  , 2.50
  , 4
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1582
  , 10850
  , 70
  , 15.00
  , 30
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1583
  , 10851
  , 2
  , 19.00
  , 5
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1584
  , 10851
  , 25
  , 14.00
  , 10
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1585
  , 10851
  , 57
  , 19.50
  , 10
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1586
  , 10851
  , 59
  , 55.00
  , 42
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1587
  , 10852
  , 2
  , 19.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1588
  , 10852
  , 17
  , 39.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1589
  , 10852
  , 62
  , 49.30
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1590
  , 10853
  , 18
  , 62.50
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1591
  , 10854
  , 10
  , 31.00
  , 100
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1592
  , 10854
  , 13
  , 6.00
  , 65
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1593
  , 10855
  , 16
  , 17.45
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1594
  , 10855
  , 31
  , 12.50
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1595
  , 10855
  , 56
  , 38.00
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1596
  , 10855
  , 65
  , 21.05
  , 15
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1597
  , 10856
  , 2
  , 19.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1598
  , 10856
  , 42
  , 14.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1599
  , 10857
  , 3
  , 10.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1600
  , 10857
  , 26
  , 31.23
  , 35
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1601
  , 10857
  , 29
  , 123.79
  , 10
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1602
  , 10858
  , 7
  , 30.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1603
  , 10858
  , 27
  , 43.90
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1604
  , 10858
  , 70
  , 15.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1605
  , 10859
  , 24
  , 4.50
  , 40
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1606
  , 10859
  , 54
  , 7.45
  , 35
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1607
  , 10859
  , 64
  , 33.25
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1608
  , 10860
  , 51
  , 53.00
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1609
  , 10860
  , 76
  , 18.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1610
  , 10861
  , 17
  , 39.00
  , 42
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1611
  , 10861
  , 18
  , 62.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1612
  , 10861
  , 21
  , 10.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1613
  , 10861
  , 33
  , 2.50
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1614
  , 10861
  , 62
  , 49.30
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1615
  , 10862
  , 11
  , 21.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1616
  , 10862
  , 52
  , 7.00
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1617
  , 10863
  , 1
  , 18.00
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1618
  , 10863
  , 58
  , 13.25
  , 12
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1619
  , 10864
  , 35
  , 18.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1620
  , 10864
  , 67
  , 14.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1621
  , 10865
  , 38
  , 263.50
  , 60
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1622
  , 10865
  , 39
  , 18.00
  , 80
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1623
  , 10866
  , 2
  , 19.00
  , 21
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1624
  , 10866
  , 24
  , 4.50
  , 6
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1625
  , 10866
  , 30
  , 25.89
  , 40
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1626
  , 10867
  , 53
  , 32.80
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1627
  , 10868
  , 26
  , 31.23
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1628
  , 10868
  , 35
  , 18.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1629
  , 10868
  , 49
  , 20.00
  , 42
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1630
  , 10869
  , 1
  , 18.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1631
  , 10869
  , 11
  , 21.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1632
  , 10869
  , 23
  , 9.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1633
  , 10869
  , 68
  , 12.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1634
  , 10870
  , 35
  , 18.00
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1635
  , 10870
  , 51
  , 53.00
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1636
  , 10871
  , 6
  , 25.00
  , 50
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1637
  , 10871
  , 16
  , 17.45
  , 12
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1638
  , 10871
  , 17
  , 39.00
  , 16
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1639
  , 10872
  , 55
  , 24.00
  , 10
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1640
  , 10872
  , 62
  , 49.30
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1641
  , 10872
  , 64
  , 33.25
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1642
  , 10872
  , 65
  , 21.05
  , 21
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1643
  , 10873
  , 21
  , 10.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1644
  , 10873
  , 28
  , 45.60
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1645
  , 10874
  , 10
  , 31.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1646
  , 10875
  , 19
  , 9.20
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1647
  , 10875
  , 47
  , 9.50
  , 21
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1648
  , 10875
  , 49
  , 20.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1649
  , 10876
  , 46
  , 12.00
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1650
  , 10876
  , 64
  , 33.25
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1651
  , 10877
  , 16
  , 17.45
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1652
  , 10877
  , 18
  , 62.50
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1653
  , 10878
  , 20
  , 81.00
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1654
  , 10879
  , 40
  , 18.40
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1655
  , 10879
  , 65
  , 21.05
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1656
  , 10879
  , 76
  , 18.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1657
  , 10880
  , 23
  , 9.00
  , 30
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1658
  , 10880
  , 61
  , 28.50
  , 30
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1659
  , 10880
  , 70
  , 15.00
  , 50
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1660
  , 10881
  , 73
  , 15.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1661
  , 10882
  , 42
  , 14.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1662
  , 10882
  , 49
  , 20.00
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1663
  , 10882
  , 54
  , 7.45
  , 32
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1664
  , 10883
  , 24
  , 4.50
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1665
  , 10884
  , 21
  , 10.00
  , 40
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1666
  , 10884
  , 56
  , 38.00
  , 21
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1667
  , 10884
  , 65
  , 21.05
  , 12
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1668
  , 10885
  , 2
  , 19.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1669
  , 10885
  , 24
  , 4.50
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1670
  , 10885
  , 70
  , 15.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1671
  , 10885
  , 77
  , 13.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1672
  , 10886
  , 10
  , 31.00
  , 70
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1673
  , 10886
  , 31
  , 12.50
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1674
  , 10886
  , 77
  , 13.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1675
  , 10887
  , 25
  , 14.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1676
  , 10888
  , 2
  , 19.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1677
  , 10888
  , 68
  , 12.50
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1678
  , 10889
  , 11
  , 21.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1679
  , 10889
  , 38
  , 263.50
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1680
  , 10890
  , 17
  , 39.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1681
  , 10890
  , 34
  , 14.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1682
  , 10890
  , 41
  , 9.65
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1683
  , 10891
  , 30
  , 25.89
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1684
  , 10892
  , 59
  , 55.00
  , 40
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1685
  , 10893
  , 8
  , 40.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1686
  , 10893
  , 24
  , 4.50
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1687
  , 10893
  , 29
  , 123.79
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1688
  , 10893
  , 30
  , 25.89
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1689
  , 10893
  , 36
  , 19.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1690
  , 10894
  , 13
  , 6.00
  , 28
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1691
  , 10894
  , 69
  , 36.00
  , 50
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1692
  , 10894
  , 75
  , 7.75
  , 120
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1693
  , 10895
  , 24
  , 4.50
  , 110
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1694
  , 10895
  , 39
  , 18.00
  , 45
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1695
  , 10895
  , 40
  , 18.40
  , 91
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1696
  , 10895
  , 60
  , 34.00
  , 100
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1697
  , 10896
  , 45
  , 9.50
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1698
  , 10896
  , 56
  , 38.00
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1699
  , 10897
  , 29
  , 123.79
  , 80
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1700
  , 10897
  , 30
  , 25.89
  , 36
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1701
  , 10898
  , 13
  , 6.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1702
  , 10899
  , 39
  , 18.00
  , 8
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1703
  , 10900
  , 70
  , 15.00
  , 3
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1704
  , 10901
  , 41
  , 9.65
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1705
  , 10901
  , 71
  , 21.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1706
  , 10902
  , 55
  , 24.00
  , 30
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1707
  , 10902
  , 62
  , 49.30
  , 6
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1708
  , 10903
  , 13
  , 6.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1709
  , 10903
  , 65
  , 21.05
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1710
  , 10903
  , 68
  , 12.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1711
  , 10904
  , 58
  , 13.25
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1712
  , 10904
  , 62
  , 49.30
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1713
  , 10905
  , 1
  , 18.00
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1714
  , 10906
  , 61
  , 28.50
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1715
  , 10907
  , 75
  , 7.75
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1716
  , 10908
  , 7
  , 30.00
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1717
  , 10908
  , 52
  , 7.00
  , 14
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1718
  , 10909
  , 7
  , 30.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1719
  , 10909
  , 16
  , 17.45
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1720
  , 10909
  , 41
  , 9.65
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1721
  , 10910
  , 19
  , 9.20
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1722
  , 10910
  , 49
  , 20.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1723
  , 10910
  , 61
  , 28.50
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1724
  , 10911
  , 1
  , 18.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1725
  , 10911
  , 17
  , 39.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1726
  , 10911
  , 67
  , 14.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1727
  , 10912
  , 11
  , 21.00
  , 40
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1728
  , 10912
  , 29
  , 123.79
  , 60
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1729
  , 10913
  , 4
  , 22.00
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1730
  , 10913
  , 33
  , 2.50
  , 40
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1731
  , 10913
  , 58
  , 13.25
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1732
  , 10914
  , 71
  , 21.50
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1733
  , 10915
  , 17
  , 39.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1734
  , 10915
  , 33
  , 2.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1735
  , 10915
  , 54
  , 7.45
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1736
  , 10916
  , 16
  , 17.45
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1737
  , 10916
  , 32
  , 32.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1738
  , 10916
  , 57
  , 19.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1739
  , 10917
  , 30
  , 25.89
  , 1
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1740
  , 10917
  , 60
  , 34.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1741
  , 10918
  , 1
  , 18.00
  , 60
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1742
  , 10918
  , 60
  , 34.00
  , 25
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1743
  , 10919
  , 16
  , 17.45
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1744
  , 10919
  , 25
  , 14.00
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1745
  , 10919
  , 40
  , 18.40
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1746
  , 10920
  , 50
  , 16.25
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1747
  , 10921
  , 35
  , 18.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1748
  , 10921
  , 63
  , 43.90
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1749
  , 10922
  , 17
  , 39.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1750
  , 10922
  , 24
  , 4.50
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1751
  , 10923
  , 42
  , 14.00
  , 10
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1752
  , 10923
  , 43
  , 46.00
  , 10
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1753
  , 10923
  , 67
  , 14.00
  , 24
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1754
  , 10924
  , 10
  , 31.00
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1755
  , 10924
  , 28
  , 45.60
  , 30
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1756
  , 10924
  , 75
  , 7.75
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1757
  , 10925
  , 36
  , 19.00
  , 25
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1758
  , 10925
  , 52
  , 7.00
  , 12
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1759
  , 10926
  , 11
  , 21.00
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1760
  , 10926
  , 13
  , 6.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1761
  , 10926
  , 19
  , 9.20
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1762
  , 10926
  , 72
  , 34.80
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1763
  , 10927
  , 20
  , 81.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1764
  , 10927
  , 52
  , 7.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1765
  , 10927
  , 76
  , 18.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1766
  , 10928
  , 47
  , 9.50
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1767
  , 10928
  , 76
  , 18.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1768
  , 10929
  , 21
  , 10.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1769
  , 10929
  , 75
  , 7.75
  , 49
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1770
  , 10929
  , 77
  , 13.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1771
  , 10930
  , 21
  , 10.00
  , 36
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1772
  , 10930
  , 27
  , 43.90
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1773
  , 10930
  , 55
  , 24.00
  , 25
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1774
  , 10930
  , 58
  , 13.25
  , 30
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1775
  , 10931
  , 13
  , 6.00
  , 42
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1776
  , 10931
  , 57
  , 19.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1777
  , 10932
  , 16
  , 17.45
  , 30
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1778
  , 10932
  , 62
  , 49.30
  , 14
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1779
  , 10932
  , 72
  , 34.80
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1780
  , 10932
  , 75
  , 7.75
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1781
  , 10933
  , 53
  , 32.80
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1782
  , 10933
  , 61
  , 28.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1783
  , 10934
  , 6
  , 25.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1784
  , 10935
  , 1
  , 18.00
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1785
  , 10935
  , 18
  , 62.50
  , 4
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1786
  , 10935
  , 23
  , 9.00
  , 8
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1787
  , 10936
  , 36
  , 19.00
  , 30
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1788
  , 10937
  , 28
  , 45.60
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1789
  , 10937
  , 34
  , 14.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1790
  , 10938
  , 13
  , 6.00
  , 20
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1791
  , 10938
  , 43
  , 46.00
  , 24
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1792
  , 10938
  , 60
  , 34.00
  , 49
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1793
  , 10938
  , 71
  , 21.50
  , 35
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1794
  , 10939
  , 2
  , 19.00
  , 10
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1795
  , 10939
  , 67
  , 14.00
  , 40
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1796
  , 10940
  , 7
  , 30.00
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1797
  , 10940
  , 13
  , 6.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1798
  , 10941
  , 31
  , 12.50
  , 44
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1799
  , 10941
  , 62
  , 49.30
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1800
  , 10941
  , 68
  , 12.50
  , 80
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1801
  , 10941
  , 72
  , 34.80
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1802
  , 10942
  , 49
  , 20.00
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1803
  , 10943
  , 13
  , 6.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1804
  , 10943
  , 22
  , 21.00
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1805
  , 10943
  , 46
  , 12.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1806
  , 10944
  , 11
  , 21.00
  , 5
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1807
  , 10944
  , 44
  , 19.45
  , 18
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1808
  , 10944
  , 56
  , 38.00
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1809
  , 10945
  , 13
  , 6.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1810
  , 10945
  , 31
  , 12.50
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1811
  , 10946
  , 10
  , 31.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1812
  , 10946
  , 24
  , 4.50
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1813
  , 10946
  , 77
  , 13.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1814
  , 10947
  , 59
  , 55.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1815
  , 10948
  , 50
  , 16.25
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1816
  , 10948
  , 51
  , 53.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1817
  , 10948
  , 55
  , 24.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1818
  , 10949
  , 6
  , 25.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1819
  , 10949
  , 10
  , 31.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1820
  , 10949
  , 17
  , 39.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1821
  , 10949
  , 62
  , 49.30
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1822
  , 10950
  , 4
  , 22.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1823
  , 10951
  , 33
  , 2.50
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1824
  , 10951
  , 41
  , 9.65
  , 6
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1825
  , 10951
  , 75
  , 7.75
  , 50
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1826
  , 10952
  , 6
  , 25.00
  , 16
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1827
  , 10952
  , 28
  , 45.60
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1828
  , 10953
  , 20
  , 81.00
  , 50
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1829
  , 10953
  , 31
  , 12.50
  , 50
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1830
  , 10954
  , 16
  , 17.45
  , 28
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1831
  , 10954
  , 31
  , 12.50
  , 25
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1832
  , 10954
  , 45
  , 9.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1833
  , 10954
  , 60
  , 34.00
  , 24
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1834
  , 10955
  , 75
  , 7.75
  , 12
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1835
  , 10956
  , 21
  , 10.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1836
  , 10956
  , 47
  , 9.50
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1837
  , 10956
  , 51
  , 53.00
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1838
  , 10957
  , 30
  , 25.89
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1839
  , 10957
  , 35
  , 18.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1840
  , 10957
  , 64
  , 33.25
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1841
  , 10958
  , 5
  , 21.35
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1842
  , 10958
  , 7
  , 30.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1843
  , 10958
  , 72
  , 34.80
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1844
  , 10959
  , 75
  , 7.75
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1845
  , 10960
  , 24
  , 4.50
  , 10
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1846
  , 10960
  , 41
  , 9.65
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1847
  , 10961
  , 52
  , 7.00
  , 6
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1848
  , 10961
  , 76
  , 18.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1849
  , 10962
  , 7
  , 30.00
  , 45
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1850
  , 10962
  , 13
  , 6.00
  , 77
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1851
  , 10962
  , 53
  , 32.80
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1852
  , 10962
  , 69
  , 36.00
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1853
  , 10962
  , 76
  , 18.00
  , 44
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1854
  , 10963
  , 60
  , 34.00
  , 2
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1855
  , 10964
  , 18
  , 62.50
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1856
  , 10964
  , 38
  , 263.50
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1857
  , 10964
  , 69
  , 36.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1858
  , 10965
  , 51
  , 53.00
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1859
  , 10966
  , 37
  , 26.00
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1860
  , 10966
  , 56
  , 38.00
  , 12
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1861
  , 10966
  , 62
  , 49.30
  , 12
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1862
  , 10967
  , 19
  , 9.20
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1863
  , 10967
  , 49
  , 20.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1864
  , 10968
  , 12
  , 38.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1865
  , 10968
  , 24
  , 4.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1866
  , 10968
  , 64
  , 33.25
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1867
  , 10969
  , 46
  , 12.00
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1868
  , 10970
  , 52
  , 7.00
  , 40
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1869
  , 10971
  , 29
  , 123.79
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1870
  , 10972
  , 17
  , 39.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1871
  , 10972
  , 33
  , 2.50
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1872
  , 10973
  , 26
  , 31.23
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1873
  , 10973
  , 41
  , 9.65
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1874
  , 10973
  , 75
  , 7.75
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1875
  , 10974
  , 63
  , 43.90
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1876
  , 10975
  , 8
  , 40.00
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1877
  , 10975
  , 75
  , 7.75
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1878
  , 10976
  , 28
  , 45.60
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1879
  , 10977
  , 39
  , 18.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1880
  , 10977
  , 47
  , 9.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1881
  , 10977
  , 51
  , 53.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1882
  , 10977
  , 63
  , 43.90
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1883
  , 10978
  , 8
  , 40.00
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1884
  , 10978
  , 21
  , 10.00
  , 40
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1885
  , 10978
  , 40
  , 18.40
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1886
  , 10978
  , 44
  , 19.45
  , 6
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1887
  , 10979
  , 7
  , 30.00
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1888
  , 10979
  , 12
  , 38.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1889
  , 10979
  , 24
  , 4.50
  , 80
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1890
  , 10979
  , 27
  , 43.90
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1891
  , 10979
  , 31
  , 12.50
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1892
  , 10979
  , 63
  , 43.90
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1893
  , 10980
  , 75
  , 7.75
  , 40
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1894
  , 10981
  , 38
  , 263.50
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1895
  , 10982
  , 7
  , 30.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1896
  , 10982
  , 43
  , 46.00
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1897
  , 10983
  , 13
  , 6.00
  , 84
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1898
  , 10983
  , 57
  , 19.50
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1899
  , 10984
  , 16
  , 17.45
  , 55
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1900
  , 10984
  , 24
  , 4.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1901
  , 10984
  , 36
  , 19.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1902
  , 10985
  , 16
  , 17.45
  , 36
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1903
  , 10985
  , 18
  , 62.50
  , 8
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1904
  , 10985
  , 32
  , 32.00
  , 35
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1905
  , 10986
  , 11
  , 21.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1906
  , 10986
  , 20
  , 81.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1907
  , 10986
  , 76
  , 18.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1908
  , 10986
  , 77
  , 13.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1909
  , 10987
  , 7
  , 30.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1910
  , 10987
  , 43
  , 46.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1911
  , 10987
  , 72
  , 34.80
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1912
  , 10988
  , 7
  , 30.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1913
  , 10988
  , 62
  , 49.30
  , 40
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1914
  , 10989
  , 6
  , 25.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1915
  , 10989
  , 11
  , 21.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1916
  , 10989
  , 41
  , 9.65
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1917
  , 10990
  , 21
  , 10.00
  , 65
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1918
  , 10990
  , 34
  , 14.00
  , 60
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1919
  , 10990
  , 55
  , 24.00
  , 65
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1920
  , 10990
  , 61
  , 28.50
  , 66
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1921
  , 10991
  , 2
  , 19.00
  , 50
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1922
  , 10991
  , 70
  , 15.00
  , 20
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1923
  , 10991
  , 76
  , 18.00
  , 90
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1924
  , 10992
  , 72
  , 34.80
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1925
  , 10993
  , 29
  , 123.79
  , 50
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1926
  , 10993
  , 41
  , 9.65
  , 35
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1927
  , 10994
  , 59
  , 55.00
  , 18
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1928
  , 10995
  , 51
  , 53.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1929
  , 10995
  , 60
  , 34.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1930
  , 10996
  , 42
  , 14.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1931
  , 10997
  , 32
  , 32.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1932
  , 10997
  , 46
  , 12.00
  , 20
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1933
  , 10997
  , 52
  , 7.00
  , 20
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1934
  , 10998
  , 24
  , 4.50
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1935
  , 10998
  , 61
  , 28.50
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1936
  , 10998
  , 74
  , 10.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1937
  , 10998
  , 75
  , 7.75
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1938
  , 10999
  , 41
  , 9.65
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1939
  , 10999
  , 51
  , 53.00
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1940
  , 10999
  , 77
  , 13.00
  , 21
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1941
  , 11000
  , 4
  , 22.00
  , 25
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1942
  , 11000
  , 24
  , 4.50
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1943
  , 11000
  , 77
  , 13.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1944
  , 11001
  , 7
  , 30.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1945
  , 11001
  , 22
  , 21.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1946
  , 11001
  , 46
  , 12.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1947
  , 11001
  , 55
  , 24.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1948
  , 11002
  , 13
  , 6.00
  , 56
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1949
  , 11002
  , 35
  , 18.00
  , 15
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1950
  , 11002
  , 42
  , 14.00
  , 24
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1951
  , 11002
  , 55
  , 24.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1952
  , 11003
  , 1
  , 18.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1953
  , 11003
  , 40
  , 18.40
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1954
  , 11003
  , 52
  , 7.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1955
  , 11004
  , 26
  , 31.23
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1956
  , 11004
  , 76
  , 18.00
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1957
  , 11005
  , 1
  , 18.00
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1958
  , 11005
  , 59
  , 55.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1959
  , 11006
  , 1
  , 18.00
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1960
  , 11006
  , 29
  , 123.79
  , 2
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1961
  , 11007
  , 8
  , 40.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1962
  , 11007
  , 29
  , 123.79
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1963
  , 11007
  , 42
  , 14.00
  , 14
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1964
  , 11008
  , 28
  , 45.60
  , 70
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1965
  , 11008
  , 34
  , 14.00
  , 90
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1966
  , 11008
  , 71
  , 21.50
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1967
  , 11009
  , 24
  , 4.50
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1968
  , 11009
  , 36
  , 19.00
  , 18
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1969
  , 11009
  , 60
  , 34.00
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1970
  , 11010
  , 7
  , 30.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1971
  , 11010
  , 24
  , 4.50
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1972
  , 11011
  , 58
  , 13.25
  , 40
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1973
  , 11011
  , 71
  , 21.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1974
  , 11012
  , 19
  , 9.20
  , 50
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1975
  , 11012
  , 60
  , 34.00
  , 36
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1976
  , 11012
  , 71
  , 21.50
  , 60
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1977
  , 11013
  , 23
  , 9.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1978
  , 11013
  , 42
  , 14.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1979
  , 11013
  , 45
  , 9.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1980
  , 11013
  , 68
  , 12.50
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1981
  , 11014
  , 41
  , 9.65
  , 28
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1982
  , 11015
  , 30
  , 25.89
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1983
  , 11015
  , 77
  , 13.00
  , 18
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1984
  , 11016
  , 31
  , 12.50
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1985
  , 11016
  , 36
  , 19.00
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1986
  , 11017
  , 3
  , 10.00
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1987
  , 11017
  , 59
  , 55.00
  , 110
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1988
  , 11017
  , 70
  , 15.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1989
  , 11018
  , 12
  , 38.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1990
  , 11018
  , 18
  , 62.50
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1991
  , 11018
  , 56
  , 38.00
  , 5
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1992
  , 11019
  , 46
  , 12.00
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1993
  , 11019
  , 49
  , 20.00
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1994
  , 11020
  , 10
  , 31.00
  , 24
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1995
  , 11021
  , 2
  , 19.00
  , 11
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1996
  , 11021
  , 20
  , 81.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1997
  , 11021
  , 26
  , 31.23
  , 63
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1998
  , 11021
  , 51
  , 53.00
  , 44
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    1999
  , 11021
  , 72
  , 34.80
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2000
  , 11022
  , 19
  , 9.20
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2001
  , 11022
  , 69
  , 36.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2002
  , 11023
  , 7
  , 30.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2003
  , 11023
  , 43
  , 46.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2004
  , 11024
  , 26
  , 31.23
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2005
  , 11024
  , 33
  , 2.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2006
  , 11024
  , 65
  , 21.05
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2007
  , 11024
  , 71
  , 21.50
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2008
  , 11025
  , 1
  , 18.00
  , 10
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2009
  , 11025
  , 13
  , 6.00
  , 20
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2010
  , 11026
  , 18
  , 62.50
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2011
  , 11026
  , 51
  , 53.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2012
  , 11027
  , 24
  , 4.50
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2013
  , 11027
  , 62
  , 49.30
  , 21
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2014
  , 11028
  , 55
  , 24.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2015
  , 11028
  , 59
  , 55.00
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2016
  , 11029
  , 56
  , 38.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2017
  , 11029
  , 63
  , 43.90
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2018
  , 11030
  , 2
  , 19.00
  , 100
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2019
  , 11030
  , 5
  , 21.35
  , 70
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2020
  , 11030
  , 29
  , 123.79
  , 60
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2021
  , 11030
  , 59
  , 55.00
  , 100
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2022
  , 11031
  , 1
  , 18.00
  , 45
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2023
  , 11031
  , 13
  , 6.00
  , 80
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2024
  , 11031
  , 24
  , 4.50
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2025
  , 11031
  , 64
  , 33.25
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2026
  , 11031
  , 71
  , 21.50
  , 16
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2027
  , 11032
  , 36
  , 19.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2028
  , 11032
  , 38
  , 263.50
  , 25
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2029
  , 11032
  , 59
  , 55.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2030
  , 11033
  , 53
  , 32.80
  , 70
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2031
  , 11033
  , 69
  , 36.00
  , 36
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2032
  , 11034
  , 21
  , 10.00
  , 15
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2033
  , 11034
  , 44
  , 19.45
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2034
  , 11034
  , 61
  , 28.50
  , 6
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2035
  , 11035
  , 1
  , 18.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2036
  , 11035
  , 35
  , 18.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2037
  , 11035
  , 42
  , 14.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2038
  , 11035
  , 54
  , 7.45
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2039
  , 11036
  , 13
  , 6.00
  , 7
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2040
  , 11036
  , 59
  , 55.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2041
  , 11037
  , 70
  , 15.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2042
  , 11038
  , 40
  , 18.40
  , 5
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2043
  , 11038
  , 52
  , 7.00
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2044
  , 11038
  , 71
  , 21.50
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2045
  , 11039
  , 28
  , 45.60
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2046
  , 11039
  , 35
  , 18.00
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2047
  , 11039
  , 49
  , 20.00
  , 60
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2048
  , 11039
  , 57
  , 19.50
  , 28
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2049
  , 11040
  , 21
  , 10.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2050
  , 11041
  , 2
  , 19.00
  , 30
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2051
  , 11041
  , 63
  , 43.90
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2052
  , 11042
  , 44
  , 19.45
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2053
  , 11042
  , 61
  , 28.50
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2054
  , 11043
  , 11
  , 21.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2055
  , 11044
  , 62
  , 49.30
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2056
  , 11045
  , 33
  , 2.50
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2057
  , 11045
  , 51
  , 53.00
  , 24
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2058
  , 11046
  , 12
  , 38.00
  , 20
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2059
  , 11046
  , 32
  , 32.00
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2060
  , 11046
  , 35
  , 18.00
  , 18
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2061
  , 11047
  , 1
  , 18.00
  , 25
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2062
  , 11047
  , 5
  , 21.35
  , 30
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2063
  , 11048
  , 68
  , 12.50
  , 42
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2064
  , 11049
  , 2
  , 19.00
  , 10
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2065
  , 11049
  , 12
  , 38.00
  , 4
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2066
  , 11050
  , 76
  , 18.00
  , 50
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2067
  , 11051
  , 24
  , 4.50
  , 10
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2068
  , 11052
  , 43
  , 46.00
  , 30
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2069
  , 11052
  , 61
  , 28.50
  , 10
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2070
  , 11053
  , 18
  , 62.50
  , 35
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2071
  , 11053
  , 32
  , 32.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2072
  , 11053
  , 64
  , 33.25
  , 25
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2073
  , 11054
  , 33
  , 2.50
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2074
  , 11054
  , 67
  , 14.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2075
  , 11055
  , 24
  , 4.50
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2076
  , 11055
  , 25
  , 14.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2077
  , 11055
  , 51
  , 53.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2078
  , 11055
  , 57
  , 19.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2079
  , 11056
  , 7
  , 30.00
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2080
  , 11056
  , 55
  , 24.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2081
  , 11056
  , 60
  , 34.00
  , 50
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2082
  , 11057
  , 70
  , 15.00
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2083
  , 11058
  , 21
  , 10.00
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2084
  , 11058
  , 60
  , 34.00
  , 21
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2085
  , 11058
  , 61
  , 28.50
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2086
  , 11059
  , 13
  , 6.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2087
  , 11059
  , 17
  , 39.00
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2088
  , 11059
  , 60
  , 34.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2089
  , 11060
  , 60
  , 34.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2090
  , 11060
  , 77
  , 13.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2091
  , 11061
  , 60
  , 34.00
  , 15
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2092
  , 11062
  , 53
  , 32.80
  , 10
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2093
  , 11062
  , 70
  , 15.00
  , 12
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2094
  , 11063
  , 34
  , 14.00
  , 30
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2095
  , 11063
  , 40
  , 18.40
  , 40
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2096
  , 11063
  , 41
  , 9.65
  , 30
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2097
  , 11064
  , 17
  , 39.00
  , 77
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2098
  , 11064
  , 41
  , 9.65
  , 12
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2099
  , 11064
  , 53
  , 32.80
  , 25
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2100
  , 11064
  , 55
  , 24.00
  , 4
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2101
  , 11064
  , 68
  , 12.50
  , 55
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2102
  , 11065
  , 30
  , 25.89
  , 4
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2103
  , 11065
  , 54
  , 7.45
  , 20
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2104
  , 11066
  , 16
  , 17.45
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2105
  , 11066
  , 19
  , 9.20
  , 42
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2106
  , 11066
  , 34
  , 14.00
  , 35
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2107
  , 11067
  , 41
  , 9.65
  , 9
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2108
  , 11068
  , 28
  , 45.60
  , 8
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2109
  , 11068
  , 43
  , 46.00
  , 36
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2110
  , 11068
  , 77
  , 13.00
  , 28
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2111
  , 11069
  , 39
  , 18.00
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2112
  , 11070
  , 1
  , 18.00
  , 40
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2113
  , 11070
  , 2
  , 19.00
  , 20
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2114
  , 11070
  , 16
  , 17.45
  , 30
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2115
  , 11070
  , 31
  , 12.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2116
  , 11071
  , 7
  , 30.00
  , 15
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2117
  , 11071
  , 13
  , 6.00
  , 10
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2118
  , 11072
  , 2
  , 19.00
  , 8
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2119
  , 11072
  , 41
  , 9.65
  , 40
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2120
  , 11072
  , 50
  , 16.25
  , 22
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2121
  , 11072
  , 64
  , 33.25
  , 130
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2122
  , 11073
  , 11
  , 21.00
  , 10
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2123
  , 11073
  , 24
  , 4.50
  , 20
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2124
  , 11074
  , 16
  , 17.45
  , 14
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2125
  , 11075
  , 2
  , 19.00
  , 10
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2126
  , 11075
  , 46
  , 12.00
  , 30
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2127
  , 11075
  , 76
  , 18.00
  , 2
  , 0.15
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2128
  , 11076
  , 6
  , 25.00
  , 20
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2129
  , 11076
  , 14
  , 23.25
  , 20
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2130
  , 11076
  , 19
  , 9.20
  , 10
  , 0.25
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2131
  , 11077
  , 2
  , 19.00
  , 24
  , 0.2
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2132
  , 11077
  , 3
  , 10.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2133
  , 11077
  , 4
  , 22.00
  , 1
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2134
  , 11077
  , 6
  , 25.00
  , 1
  , 0.02
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2135
  , 11077
  , 7
  , 30.00
  , 1
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2136
  , 11077
  , 8
  , 40.00
  , 2
  , 0.1
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2137
  , 11077
  , 10
  , 31.00
  , 1
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2138
  , 11077
  , 12
  , 38.00
  , 2
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2139
  , 11077
  , 13
  , 6.00
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2140
  , 11077
  , 14
  , 23.25
  , 1
  , 0.03
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2141
  , 11077
  , 16
  , 17.45
  , 2
  , 0.03
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2142
  , 11077
  , 20
  , 81.00
  , 1
  , 0.04
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2143
  , 11077
  , 23
  , 9.00
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2144
  , 11077
  , 32
  , 32.00
  , 1
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2145
  , 11077
  , 39
  , 18.00
  , 2
  , 0.05
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2146
  , 11077
  , 41
  , 9.65
  , 3
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2147
  , 11077
  , 46
  , 12.00
  , 3
  , 0.02
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2148
  , 11077
  , 52
  , 7.00
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2149
  , 11077
  , 55
  , 24.00
  , 2
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2150
  , 11077
  , 60
  , 34.00
  , 2
  , 0.06
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2151
  , 11077
  , 64
  , 33.25
  , 2
  , 0.03
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2152
  , 11077
  , 66
  , 17.00
  , 1
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2153
  , 11077
  , 73
  , 15.00
  , 2
  , 0.01
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2154
  , 11077
  , 75
  , 7.75
  , 4
  , 0.0
);

INSERT INTO ORDER_DETAILS
(
    ID
  , ORDER_ID
  , PRODUCT_ID
  , UNIT_PRICE
  , QUANTITY
  , DISCOUNT
)
VALUES
(
    2155
  , 11077
  , 77
  , 13.00
  , 2
  , 0.0
);

UPDATE EMPLOYEES
   SET REPORTS_TO = 2
  WHERE ID = 1;
UPDATE EMPLOYEES
   SET REPORTS_TO = 2
  WHERE ID = 3;
UPDATE EMPLOYEES
   SET REPORTS_TO = 2
  WHERE ID = 4;
UPDATE EMPLOYEES
   SET REPORTS_TO = 2
  WHERE ID = 5;
UPDATE EMPLOYEES
   SET REPORTS_TO = 5
  WHERE ID = 6;
UPDATE EMPLOYEES
   SET REPORTS_TO = 5
  WHERE ID = 7;
UPDATE EMPLOYEES
   SET REPORTS_TO = 2
  WHERE ID = 8;
UPDATE EMPLOYEES
   SET REPORTS_TO = 5
  WHERE ID = 9;

UPDATE EMPLOYEES
   SET SEX = 0
  WHERE ID = 1;
UPDATE EMPLOYEES
   SET SEX = 1
  WHERE ID = 2;
UPDATE EMPLOYEES
   SET SEX = 1
  WHERE ID = 3;
UPDATE EMPLOYEES
   SET SEX = 0
  WHERE ID = 4;
UPDATE EMPLOYEES
   SET SEX = 1
  WHERE ID = 5;
UPDATE EMPLOYEES
   SET SEX = 1
  WHERE ID = 6;
UPDATE EMPLOYEES
   SET SEX = 1
  WHERE ID = 7;
UPDATE EMPLOYEES
   SET SEX = 0
  WHERE ID = 8;
UPDATE EMPLOYEES
   SET SEX = 0
  WHERE ID = 9;

--==============================================================
-- DBMS name:      ANSI Level 2
-- Created on:     2010/12/23 22:52:41
--==============================================================


--==============================================================
-- Table: EXAMPLE_CATEGORIES
--==============================================================
create table EXAMPLE_CATEGORIES (
ID                   NUMERIC(6)           not null,
CATEGORY_ID          NUMERIC(6),
LABEL                VARCHAR(50),
IS_NEW               SMALLINT             not null default 0,
IS_HOT               SMALLINT             not null default 0,
SORT_FLAG            INTEGER               default 0,
ICON                 VARCHAR(100),
URL                  VARCHAR(200),
TAGS                 VARCHAR(200),
SUMMARY              VARCHAR(1000),
primary key (ID),
foreign key (CATEGORY_ID)
      references EXAMPLE_CATEGORIES (ID)
);

--==============================================================
-- Table: EXAMPLES
--==============================================================
create table EXAMPLES (
ID                   NUMERIC(6)           not null,
LABEL                VARCHAR(100),
SORT_FLAG            INTEGER               default 0,
IS_NEW               SMALLINT             not null default 0,
IS_HOT               SMALLINT             not null default 0,
AUTHOR               VARCHAR(50),
CREATE_DATE          DATE,
LAST_MODIFY          DATE,
ICON                 VARCHAR(100),
URL                  VARCHAR(200),
TAGS                 VARCHAR(200),
SUMMARY              VARCHAR(1000),
primary key (ID)
);

--==============================================================
-- Table: CATEGORY_EXAMPLE_RELATIONS
--==============================================================
create table CATEGORY_EXAMPLE_RELATIONS (
CATEGORY_ID          NUMERIC(6)           not null,
EXAMPLE_ID           NUMERIC(6)           not null,
SORT_FLAG            INTEGER               default 0,
primary key (CATEGORY_ID, EXAMPLE_ID),
foreign key (CATEGORY_ID)
      references EXAMPLE_CATEGORIES (ID),
foreign key (EXAMPLE_ID)
      references EXAMPLES (ID)
);

--==============================================================
-- Index: I_EXAMPLES_SORT_FLAG
--==============================================================
create  index I_EXAMPLES_SORT_FLAG on CATEGORY_EXAMPLE_RELATIONS (
CATEGORY_ID ASC,
EXAMPLE_ID ASC,
SORT_FLAG ASC
);

--==============================================================
-- Index: I_EXAMPLES_LABEL
--==============================================================
create  index I_EXAMPLES_LABEL on EXAMPLES (
LABEL ASC
);

--==============================================================
-- Index: I_CATEGORIES_SORT_FLAG
--==============================================================
create  index I_CATEGORIES_SORT_FLAG on EXAMPLE_CATEGORIES (
CATEGORY_ID ASC,
SORT_FLAG ASC
);

--==============================================================
-- Table: EXAMPLE_SOURCES
--==============================================================
create table EXAMPLE_SOURCES (
ID                   NUMERIC(6)           not null,
EXAMPLE_ID           NUMERIC(6),
SORT_FLAG            INTEGER               default 0,
PATH                 VARCHAR(200),
TYPE                 VARCHAR(10),
LABEL                VARCHAR(50),
primary key (ID),
foreign key (EXAMPLE_ID)
      references EXAMPLES (ID)
);