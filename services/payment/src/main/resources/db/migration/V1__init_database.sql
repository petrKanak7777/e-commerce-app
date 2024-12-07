
create table if not exists payment
(
    id integer not null,
    amount numeric(38,2),
    order_id integer,
    payment_method character varying(255),
    created_date timestamp(6) without time zone not null,
    last_modified_date timestamp(6) without time zone,
    constraint payment_pkey primary key (id),
    CONSTRAINT payment_payment_method_check CHECK (payment_method::text = ANY (ARRAY['PAYPAL'::character varying, 'CREDIT_CARD'::character varying, 'VISA'::character varying, 'MASTER_CARD'::character varying, 'BITCOIN'::character varying]::text[]))
);

create sequence if not exists payment_seq increment by 50;

/*
CREATE TABLE IF NOT EXISTS public.payment
(
    amount numeric(38,2),
    id integer NOT NULL,
    order_id integer,
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_date timestamp(6) without time zone,
    payment_method character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT payment_pkey PRIMARY KEY (id),
    CONSTRAINT payment_payment_method_check CHECK (payment_method::text = ANY (ARRAY['PAYPAL'::character varying, 'CREDIT_CARD'::character varying, 'VISA'::character varying, 'MASTER_CARD'::character varying, 'BITCOIN'::character varying]::text[]))
)
 */