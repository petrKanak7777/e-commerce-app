-- Assuming you already have a sequence named 'customer_order_seq'
insert into customer_order (id, reference, total_amount, payment_method, customer_id)
    values (nextval('customer_order_seq'), 'reference 0 - fill it better', 2.0, 0, 'customer-001');

insert into customer_order (id, reference, total_amount, payment_method, customer_id)
    values (nextval('customer_order_seq'), 'reference 1 - fill it better', 1.0, 0, 'customer-002');


-- 51 -> 'Wireless Compact Keyboard 1', 101 -> 'Gaming Keyboard 1'

-- Assuming you already have a sequence named 'order_line_seq'
insert into order_line (id, product_id, quantity, order_id)
    values (nextval('order_line_seq'), 51, 2.0, (select id from customer_order where customer_id = 'customer-001'));

insert into order_line (id, product_id, quantity, order_id)
    values (nextval('order_line_seq'), 101, 1.0, (select id from customer_order where customer_id = 'customer-002'));
