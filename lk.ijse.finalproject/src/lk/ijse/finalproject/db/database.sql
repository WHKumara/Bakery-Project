CREATE DATABASE bakery;

USE bakery;

CREATE TABLE IF NOT EXISTS customer(

    CustID VARCHAR(5),
    CustName VARCHAR(30),
    CustContact int(10),
    CustAddress VARCHAR(20),
    CONSTRAINT PRIMARY KEY (CustID)

    );

INSERT INTO customer VALUES('C01','Saman Gamage',0752347894,'Galle');
INSERT INTO customer VALUES('C02','Siripala Udawatta',0774568524,'Gonnapinuwala');

CREATE TABLE IF NOT EXISTS employee(

    EmpID VARCHAR(5),
    EmpName VARCHAR(50),
    EmpContact int(10),
    EmpAddress VARCHAR(20),
    EmpEmail VARCHAR(50),
    CONSTRAINT PRIMARY KEY (EmpID)

    );

INSERT INTO employee VALUES('E01','Kamala',0752347894,'Galle','hhh@gmail.com');
INSERT INTO employee VALUES('E02','Wimala',0774568524,'Gonnapinuwala','hhh@gmail.com');


CREATE TABLE IF NOT EXISTS salary(

    SalaryID VARCHAR(4),
    Date DATE,
    SalaryAmount DECIMAL(10,2),
    EmpID VARCHAR(5),
    CONSTRAINT PRIMARY KEY (SalaryID),
    CONSTRAINT FOREIGN KEY (EmpID) REFERENCES employee(EmpID)
    ON DELETE CASCADE ON UPDATE CASCADE

    );

INSERT INTO salary VALUES('S01',now(),50200,'E01');
INSERT INTO salary VALUES('S02',now(),25000,'E01');

c

CREATE TABLE IF NOT EXISTS raw_material(

    MaterialID VARCHAR(5),
    type VARCHAR(20),
    Qty int(6),
    SupID VARCHAR(5),
    CONSTRAINT PRIMARY KEY (MaterialID),
    CONSTRAINT FOREIGN KEY (SupID) REFERENCES supplier(SupID)
    ON DELETE CASCADE ON UPDATE CASCADE

    );

INSERT INTO raw_material VALUES('R01','Wheat Flour',360,'S01');
INSERT INTO raw_material VALUES('R02','Sugar',152,'S02');


CREATE TABLE IF NOT EXISTS supplier(

    SupID VARCHAR(5),
    SupName VARCHAR(30),
    SupContact int(10),
    SupAddress VARCHAR(20),
    SupEmail VARCHAR(50),
    CONSTRAINT PRIMARY KEY (SupID)

    );

INSERT INTO supplier VALUES('S01','Finagal House',0752347894,'Galle','hhh@gmail.com');
INSERT INTO supplier VALUES('S02','Little Lion',0774568524,'Gonnapinuwala','hhh@gmail.com');


CREATE TABLE IF NOT EXISTS supplydetail(

    SupID VARCHAR(5),
    MaterialID VARCHAR(5),
    Qty_kg int(6),
    Date DATE,
    CONSTRAINT PRIMARY KEY (SupID,MaterialID),
    CONSTRAINT FOREIGN KEY (SupID) REFERENCES supplier(SupID)
    ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (MaterialID) REFERENCES raw_material(MaterialID)
    ON DELETE CASCADE ON UPDATE CASCADE

    );

CREATE TABLE IF NOT EXISTS payment(

    PaymentID VARCHAR(4),
    PaymentAmount DECIMAL(10,2),
    Date DATE,
    Time TIME,
    SupID VARCHAR(5),
    CONSTRAINT PRIMARY KEY (PaymentID),
    CONSTRAINT FOREIGN KEY (SupID) REFERENCES supplier(SupID)
    ON DELETE CASCADE ON UPDATE CASCADE

    );

INSERT INTO payment VALUES('P01',50000,now(),now(),'S01');
INSERT INTO payment VALUES('P02',25000,now(),now(),'S02');

CREATE TABLE IF NOT EXISTS orders(

    OrderID VARCHAR(5),
    CustID VARCHAR(5),
    Date DATE,
    Time TIME,
    Amount double,
    CONSTRAINT PRIMARY KEY (OrderID),
    CONSTRAINT FOREIGN KEY (CustID) REFERENCES customer(CustID)
    ON DELETE CASCADE ON UPDATE CASCADE

    );

CREATE TABLE IF NOT EXISTS item(

    ItemID VARCHAR(5),
    Qty INT(5),
    Detail VARCHAR(50),
    UnitPrice DECIMAL(10,2),
    CONSTRAINT PRIMARY KEY (ItemID)

    );

CREATE TABLE IF NOT EXISTS orderdetail(

    OrderID VARCHAR(5),
    ItemID VARCHAR(5),
    Detail VARCHAR(20),
    UnitPrice DECIMAL(10,2),
    Qty double,
    CONSTRAINT PRIMARY KEY (OrderID,ItemID),
    CONSTRAINT FOREIGN KEY (OrderID) REFERENCES orders(OrderID)
    ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (ItemID) REFERENCES item(ItemID)
    ON DELETE CASCADE ON UPDATE CASCADE

    );

INSERT INTO orders VALUES('O01','C01',now(),now(),5000);
INSERT INTO orderdetail VALUES('O01','I01','test',34.00,4);