-- HOA DON BAN HANG
DROP TABLE IF EXISTS hdbh_modify;
CREATE TABLE hdbh_modify (
  id  bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY,
  mode int DEFAULT 0, -- 2: Dieu chinh tang, 3: Dieu chinh giam
  fast_id varchar(15),
  fast_invoice_no varchar(12),
  e_invoice_no varchar(12),
  unit_code varchar(8),
  customer_id varchar(8),
  buyer_name nvarchar(128),
  customer_name nvarchar(128),
  customer_tax_code varchar(18),
  address nvarchar(128),
  invoice_date smalldatetime,
  invoice_modify_date smalldatetime,
  description nvarchar(128),
  total_quantity varchar(25),
  total_tax_amount varchar(25),
  subtotal_amount varchar(25),
  total_amount varchar(25),
  payment_method char(2),
  process_id bigint,
  status int DEFAULT 0,  -- 0-Chua lam 1-Đa gui 2-Cho thuc hien 3-Dang lam 4-That bai 5-Thanh cong
  create_by nvarchar(64) DEFAULT '',
  create_time datetime,
  update_by nvarchar(64) DEFAULT '',
  update_time datetime
);

DROP TABLE IF EXISTS hdbh_modify_detail;
CREATE TABLE hdbh_modify_detail (
  id  bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY,
  hdbh_id bigint NOT NULL,
  fast_id varchar(15),
  fast_detail_id varchar(3),
  product_id varchar(16),
  product_unit nvarchar(10),
  product_name nvarchar(48),
  product_tax_id varchar(8),
  product_quantity varchar(25),
  product_price varchar(25),
  product_subtotal_amount varchar(25),
  product_tax_amount varchar(25),
  product_discount varchar(25),
  product_total_amount varchar(25),
  product_expired_date smalldatetime,
  stock_out varchar(8),
  stock_out_name nvarchar(128),
  lot_no varchar(16),
  create_by nvarchar(64) DEFAULT '',
  create_time datetime,
  update_by nvarchar(64) DEFAULT '',
  update_time datetime
);

-- HOA DON BAN HANG CHI NHANH
DROP TABLE IF EXISTS hdbh_cn_modify;
CREATE TABLE hdbh_cn_modify (
  id  bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY,
  mode int DEFAULT 0, -- 2: Dieu chinh tang, 3: Dieu chinh giam
  fast_id varchar(15),
  fast_invoice_no varchar(12),
  e_invoice_no varchar(12),
  unit_code varchar(8),
  customer_id varchar(8),
  buyer_name nvarchar(128),
  customer_name nvarchar(128),
  customer_tax_code varchar(18),
  address nvarchar(128),
  invoice_date smalldatetime,
  invoice_modify_date smalldatetime,
  description nvarchar(128),
  total_quantity varchar(25),
  total_tax_amount varchar(25),
  subtotal_amount varchar(25),
  total_amount varchar(25),
  payment_method char(2),
  process_id bigint,
  status int DEFAULT 0,  -- 0-Chua lam 1-Đa gui 2-Cho thuc hien 3-Dang lam 4-That bai 5-Thanh cong
  create_by nvarchar(64) DEFAULT '',
  create_time datetime,
  update_by nvarchar(64) DEFAULT '',
  update_time datetime
);

DROP TABLE IF EXISTS hdbh_cn_modify_detail;
CREATE TABLE hdbh_cn_modify_detail (
  id  bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY,
  hdbh_cn_id bigint NOT NULL,
  fast_id varchar(15),
  fast_detail_id varchar(3),
  product_id varchar(16),
  product_unit nvarchar(10),
  product_name nvarchar(48),
  product_tax_id varchar(8),
  product_quantity varchar(25),
  product_price varchar(25),
  product_subtotal_amount varchar(25),
  product_tax_amount varchar(25),
  product_discount varchar(25),
  product_total_amount varchar(25),
  product_expired_date smalldatetime,
  stock_out varchar(8),
  stock_out_name nvarchar(128),
  lot_no varchar(16),
  create_by nvarchar(64) DEFAULT '',
  create_time datetime,
  update_by nvarchar(64) DEFAULT '',
  update_time datetime
);

-- HOA DON DICH VU
DROP TABLE IF EXISTS hddv_modify;
CREATE TABLE hddv_modify (
  id  bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY,
  mode int DEFAULT 0, -- 2: Dieu chinh tang, 3: Dieu chinh giam
  fast_id varchar(15),
  fast_invoice_no varchar(12),
  e_invoice_no varchar(12),
  unit_code varchar(8),
  customer_id varchar(8),
  buyer_name nvarchar(128),
  customer_name nvarchar(128),
  customer_tax_code varchar(18),
  address nvarchar(128),
  invoice_date smalldatetime,
  invoice_modify_date smalldatetime,
  description nvarchar(128),
  total_tax_amount varchar(25),
  subtotal_amount varchar(25),
  total_amount varchar(25),
  payment_method char(2),
  process_id bigint,
  status int DEFAULT 0,  -- 0-Chua lam 1-Đa gui 2-Cho thuc hien 3-Dang lam 4-That bai 5-Thanh cong
  create_by nvarchar(64) DEFAULT '',
  create_time datetime,
  update_by nvarchar(64) DEFAULT '',
  update_time datetime
);

DROP TABLE IF EXISTS hddv_modify_detail;
CREATE TABLE hddv_modify_detail (
  id  bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY,
  hddv_id bigint NOT NULL,
  fast_id varchar(15),
  fast_detail_id varchar(3),
  product_name nvarchar(48),
  product_tax_id varchar(8),
  product_tax_amount varchar(25),
  product_subtotal_amount varchar(25),
  product_total_amount varchar(25),
  create_by nvarchar(64) DEFAULT '',
  create_time datetime,
  update_by nvarchar(64) DEFAULT '',
  update_time datetime
);
-- HOA DON XUAT TRA LAI NCC
DROP TABLE IF EXISTS hdx_ncc_modify;
CREATE TABLE hdx_ncc_modify (
  id  bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY,
  mode int DEFAULT 0, -- 2: Dieu chinh tang, 3: Dieu chinh giam
  fast_id varchar(15),
  fast_invoice_no varchar(12),
  e_invoice_no varchar(12),
  unit_code varchar(8),
  customer_id varchar(8),
  buyer_name nvarchar(128),
  customer_name nvarchar(128),
  customer_tax_code varchar(18),
  address nvarchar(128),
  invoice_date smalldatetime,
  invoice_modify_date smalldatetime,
  description nvarchar(128),
  total_quantity varchar(25),
  total_tax_amount varchar(25),
  subtotal_amount varchar(25),
  total_amount varchar(25),
  payment_method char(2),
  transporter varchar(25),
  process_id bigint,
  status int DEFAULT 0,  -- 0-Chua lam 1-Đa gui 2-Cho thuc hien 3-Dang lam 4-That bai 5-Thanh cong
  create_by nvarchar(64) DEFAULT '',
  create_time datetime,
  update_by nvarchar(64) DEFAULT '',
  update_time datetime
);

DROP TABLE IF EXISTS hdx_ncc_modify_detail;
CREATE TABLE hdx_ncc_modify_detail (
  id  bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY,
  hdxNcc_id bigint NOT NULL,
  fast_id varchar(15),
  fast_detail_id varchar(3),
  product_id varchar(16),
  product_unit nvarchar(10),
  product_name nvarchar(48),
  product_tax_id varchar(8),
  product_quantity varchar(25),
  product_price varchar(25),
  product_subtotal_amount varchar(25),
  product_tax_amount varchar(25),
  product_discount varchar(25),
  product_total_amount varchar(25),
  product_expired_date smalldatetime,
  stock_out varchar(8),
  stock_out_name nvarchar(128),
  lot_no varchar(16),
  create_by nvarchar(64) DEFAULT '',
  create_time datetime,
  update_by nvarchar(64) DEFAULT '',
  update_time datetime
);

-- HOA DON BANH HANG NOI BO
DROP TABLE IF EXISTS hdbh_nb_modify;
CREATE TABLE hdbh_nb_modify (
  id  bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY,
  mode int DEFAULT 0, -- 2: Dieu chinh tang, 3: Dieu chinh giam
  fast_id varchar(15),
  fast_invoice_no varchar(12),
  e_invoice_no varchar(12),
  unit_code varchar(8),
  customer_id varchar(8),
  buyer_name nvarchar(128),
  customer_name nvarchar(128),
  customer_tax_code varchar(18),
  address nvarchar(128),
  invoice_date smalldatetime,
  invoice_modify_date smalldatetime,
  description nvarchar(128),
  total_quantity varchar(25),
  total_tax_amount varchar(25),
  subtotal_amount varchar(25),
  total_amount varchar(25),
  transporter nvarchar(128),
  vehicle varchar(12),
  stock_in nvarchar(16),
  stock_in_name nvarchar(48),
  order_no nvarchar(16),
  order_date smalldatetime,
  process_id bigint,
  status int DEFAULT 0,  -- 0-Chua lam 1-Đa gui 2-Cho thuc hien 3-Dang lam 4-That bai 5-Thanh cong
  create_by nvarchar(64) DEFAULT '',
  create_time datetime,
  update_by nvarchar(64) DEFAULT '',
  update_time datetime
);

DROP TABLE IF EXISTS hdbh_nb_modify_detail;
CREATE TABLE hdbh_nb_modify_detail (
  id  bigint   IDENTITY(1, 1)   NOT NULL    PRIMARY KEY,
  hdbh_nb_id bigint NOT NULL,
  fast_id varchar(15),
  fast_detail_id varchar(3),
  product_id varchar(16),
  product_unit nvarchar(10),
  product_name nvarchar(48),
  product_quantity varchar(25),
  product_price varchar(25),
  product_subtotal_amount varchar(25),
  product_tax_amount varchar(25),
  product_total_amount varchar(25),
  product_expired_date smalldatetime,
  stock_out varchar(8),
  stock_out_name nvarchar(128),
  lot_no varchar(16),
  create_by nvarchar(64) DEFAULT '',
  create_time datetime,
  update_by nvarchar(64) DEFAULT '',
  update_time datetime
);
