BEGIN;

-- DROP TABLES in reverse dependency order
DROP TABLE IF EXISTS public.order_details CASCADE;
DROP TABLE IF EXISTS public.orders CASCADE;
DROP TABLE IF EXISTS public.requests CASCADE;
DROP TABLE IF EXISTS public.customer_information CASCADE;
DROP TABLE IF EXISTS public.material_length CASCADE;
DROP TABLE IF EXISTS public.material_list CASCADE;
DROP TABLE IF EXISTS public.users CASCADE;
DROP TABLE IF EXISTS public.zip_code CASCADE;
DROP TABLE IF EXISTS test.admin_users CASCADE;

-- RECREATE TABLES

CREATE TABLE IF NOT EXISTS public.customer_information (
                                                           customer_id serial NOT NULL,
                                                           first_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    address character varying(50) COLLATE pg_catalog."default" NOT NULL,
    phone_number character varying(20) COLLATE pg_catalog."default" NOT NULL,
    customer_email character varying(100) COLLATE pg_catalog."default" NOT NULL,
    zip_code character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT customer_information_pkey PRIMARY KEY (customer_id)
    );

CREATE TABLE IF NOT EXISTS public.material_length (
                                                      length_id serial NOT NULL,
                                                      material_id integer NOT NULL,
                                                      matelial_length bigint NOT NULL,
                                                      CONSTRAINT material_length_pkey PRIMARY KEY (length_id)
    );

CREATE TABLE IF NOT EXISTS public.material_list (
                                                    material_id serial NOT NULL,
                                                    material_name character varying(200) COLLATE pg_catalog."default" NOT NULL,
    material_type character varying(50) COLLATE pg_catalog."default" NOT NULL,
    material_quantity integer NOT NULL,
    material_unit character varying(10) COLLATE pg_catalog."default" NOT NULL,
    material_price double precision NOT NULL,
    CONSTRAINT material_list_pkey PRIMARY KEY (material_id)
    );

CREATE TABLE IF NOT EXISTS public.order_details (
                                                    order_id integer NOT NULL,
                                                    material_id integer NOT NULL,
                                                    quantity integer NOT NULL,
                                                    material_length integer NOT NULL,
                                                    CONSTRAINT order_details_pkey PRIMARY KEY (order_id, material_id)
    );

CREATE TABLE IF NOT EXISTS public.orders (
                                             order_id serial NOT NULL,
                                             customer_id integer NOT NULL,
                                             request_id integer NOT NULL,
                                             "order_totalPrice" double precision NOT NULL,
                                             CONSTRAINT orders_pkey PRIMARY KEY (order_id)
    );

CREATE TABLE IF NOT EXISTS public.requests (
                                               request_id serial NOT NULL,
                                               request_length integer NOT NULL,
                                               request_width integer NOT NULL,
                                               request_height integer NOT NULL,
                                               customer_id integer NOT NULL,
                                               CONSTRAINT requests_pkey PRIMARY KEY (request_id)
    );

CREATE TABLE IF NOT EXISTS public.users (
                                            email character varying(100) COLLATE pg_catalog."default" NOT NULL,
    password character varying(100) COLLATE pg_catalog."default" NOT NULL,
    admin boolean DEFAULT false,
    CONSTRAINT users_pkey PRIMARY KEY (email)
    );

CREATE TABLE IF NOT EXISTS public.zip_code (
                                               zip_code character varying(10) COLLATE pg_catalog."default" NOT NULL,
    city character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT zip_code_pkey PRIMARY KEY (zip_code)
    );

CREATE TABLE IF NOT EXISTS test.admin_users (
                                                email character varying(100) COLLATE pg_catalog."default" NOT NULL,
    password character varying(100) COLLATE pg_catalog."default" NOT NULL,
    admin boolean DEFAULT false,
    CONSTRAINT admin_users_pkey PRIMARY KEY (email)
    );

-- Foreign Keys

ALTER TABLE IF EXISTS public.customer_information
    ADD CONSTRAINT fk_customer_zip FOREIGN KEY (zip_code)
    REFERENCES public.zip_code (zip_code);

ALTER TABLE IF EXISTS public.order_details
    ADD CONSTRAINT fk_orderdetails_material FOREIGN KEY (material_id)
    REFERENCES public.material_list (material_id);

ALTER TABLE IF EXISTS public.order_details
    ADD CONSTRAINT fk_orderdetails_order FOREIGN KEY (order_id)
    REFERENCES public.orders (order_id);

ALTER TABLE IF EXISTS public.orders
    ADD CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id)
    REFERENCES public.customer_information (customer_id);

ALTER TABLE IF EXISTS public.orders
    ADD CONSTRAINT fk_orders_request FOREIGN KEY (request_id)
    REFERENCES public.requests (request_id);

ALTER TABLE IF EXISTS public.requests
    ADD CONSTRAINT fk_requests_customer FOREIGN KEY (customer_id)
    REFERENCES public.customer_information (customer_id);

END;
