
create table if not exists customer_order
(
    id integer not null,
    reference varchar(255),
    total_amount numeric(38, 2),
    payment_method smallint,
    created_date timestamp(6) without time zone not null default NOW(), -- no time-zone give time in UTC!
    last_modified_date timestamp(6) without time zone not null default NOW(), -- no time-zone give time in UTC!
    customer_id varchar(255),
    constraint customer_order_pkey primary key (id),
    constraint customer_order_payment_method_check check (payment_method >= 0 and payment_method <= 4)
);

create table if not exists order_line
(
    id integer not null,
    product_id integer not null,
    quantity double precision not null,
    order_id integer
    constraint fk_order_id references customer_order,
    constraint order_line_pkey primary key (id)
);

create sequence if not exists customer_order_seq increment by 50;
create sequence if not exists order_line_seq increment by 50;

/*
 CREATE TABLE IF NOT EXISTS public.customer_order
(
    id integer NOT NULL,
    payment_method smallint,
    total_amount numeric(38,2),
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_date timestamp(6) without time zone,
    customer_id character varying(255) COLLATE pg_catalog."default",
    reference character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT customer_order_pkey PRIMARY KEY (id),
    CONSTRAINT customer_order_payment_method_check CHECK (payment_method >= 0 AND payment_method <= 4)
)

CREATE TABLE IF NOT EXISTS public.order_line
(
    id integer NOT NULL,
    order_id integer,
    product_id integer,
    quantity double precision NOT NULL,
    CONSTRAINT order_line_pkey PRIMARY KEY (id),
    CONSTRAINT fkhx2sh9w4yimwp265ak68pa7i5 FOREIGN KEY (order_id)
        REFERENCES public.customer_order (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
 */