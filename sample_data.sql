create database if not exists java_cars;

use java_cars;

create table customers
(
    id         int auto_increment
        primary key,
    last_name  text         not null,
    first_name text         not null,
    birthdate  date         not null,
    email      varchar(200) not null,
    phone      varchar(20)  null,
    company    text         null,
    constraint unique_email
        unique (email)
);

INSERT INTO java_cars.customers (id, last_name, first_name, birthdate, email, phone, company) VALUES (26, 'Loïc', 'Etienne', '2000-01-01', 'loic.etienne@corp.com', '0612345678', 'Some company');
INSERT INTO java_cars.customers (id, last_name, first_name, birthdate, email, phone, company) VALUES (27, 'Someone', 'Anyone', '2000-01-01', 'someone.anyone@corp1.com', '0612345678', 'Other company');
INSERT INTO java_cars.customers (id, last_name, first_name, birthdate, email, phone, company) VALUES (28, 'Someone', 'Anyone', '2000-01-01', 'someone.anyone@corp2.com', '0612345678', 'Another company');
INSERT INTO java_cars.customers (id, last_name, first_name, birthdate, email, phone, company) VALUES (29, 'Someone', 'Anyone', '2000-01-01', 'someone.anyone@corp3.com', '0612345678', '');
INSERT INTO java_cars.customers (id, last_name, first_name, birthdate, email, phone, company) VALUES (30, 'Someone', 'Anyone', '2000-01-01', 'someone.anyone@corp4.com', '0612345678', 'Something Exports LLC');

create table vehicles
(
    id              int auto_increment
        primary key,
    model           text charset utf8mb4                      not null,
    brand           text charset utf8mb4                      not null,
    type            varchar(15) charset utf8mb4 default 'car' not null,
    meter           float                                     not null,
    publishing_date date                                      not null,
    location        text charset utf8mb4                      not null,
    country_code    varchar(2) charset utf8mb4                not null,
    price           float                                     not null,
    currency        varchar(3) charset utf8mb4  default 'EUR' not null,
    stock           int                         default 0     not null,
    discounted      tinyint(1)                  default 0     not null,
    image           text                                      null,
    constraint check_type
        check ((type = 'car') or (type = 'motorcycle'))
)
collate=utf8mb4_unicode_ci;

INSERT INTO java_cars.vehicles (id, model, brand, type, meter, publishing_date, location, country_code, price, currency, stock, discounted, image) VALUES (4, 'Chiron', 'Bugatti', 'car', 0, '2022-10-03', 'Monaco', 'MC', 3200000, 'EUR', 28, 1, 'https://www.challenges.fr/assets/img/2019/09/04/cover-r4x3w1200-5d6f9e44cf5c8-07-ss300p-ehra-lessien.jpg');
INSERT INTO java_cars.vehicles (id, model, brand, type, meter, publishing_date, location, country_code, price, currency, stock, discounted, image) VALUES (6, 'Yaris', 'Toyota', 'car', 0, '2022-10-29', 'Tokyo', 'JP', 10000, 'EUR', 15, 0, 'https://images.caradisiac.com/images/8/2/0/0/198200/S1-essai-toyota-yaris-1-0-2022-que-vaut-la-moins-chere-des-yaris-727312.jpg');
INSERT INTO java_cars.vehicles (id, model, brand, type, meter, publishing_date, location, country_code, price, currency, stock, discounted, image) VALUES (7, '107', 'Peugeot', 'car', 0, '2022-10-03', 'Paris', 'FR', 2400, 'EUR', 20, 1, 'https://www.largus.fr/images/images/peugeot-107_1.jpg');
INSERT INTO java_cars.vehicles (id, model, brand, type, meter, publishing_date, location, country_code, price, currency, stock, discounted, image) VALUES (8, 'i8', 'BMW', 'car', 0, '2022-10-12', 'Munich', 'DE', 70000, 'EUR', 1, 0, 'https://sf1.auto-moto.com/wp-content/uploads/sites/9/2019/11/bmw-i8.jpg');
INSERT INTO java_cars.vehicles (id, model, brand, type, meter, publishing_date, location, country_code, price, currency, stock, discounted, image) VALUES (9, '208', 'Peugeot', 'car', 0, '2022-10-27', 'Tours', 'FR', 5000, 'EUR', 27, 0, 'https://www.caroom.fr/guide/wp-content/uploads/2021/11/peugeot-208-dimensions-arriere.jpg');
INSERT INTO java_cars.vehicles (id, model, brand, type, meter, publishing_date, location, country_code, price, currency, stock, discounted, image) VALUES (10, '2', 'Clio', 'car', 0, '2022-10-04', 'Dakar', 'SN', 400, 'EUR', 38, 1, 'https://media.cdnws.com/_i/40104/185/1815/86/clio-2.jpeg');
INSERT INTO java_cars.vehicles (id, model, brand, type, meter, publishing_date, location, country_code, price, currency, stock, discounted, image) VALUES (11, 'Grandeur', 'Hyundai', 'car', 0, '2022-08-01', 'Seoul', 'KR', 40000, 'EUR', 71, 1, 'https://www.auto-data.net/images/f78/Hyundai-Grandeur-Azera-VI-IG-facelift-2019.jpg');
INSERT INTO java_cars.vehicles (id, model, brand, type, meter, publishing_date, location, country_code, price, currency, stock, discounted, image) VALUES (12, 'BMW', 'CE-04', 'motorcycle', 0, '2022-12-05', 'Berlin', 'DE', 50000, 'EUR', 142, 0, 'https://media.gqmagazine.fr/photos/6124b3b4a85390907a53ae42/master/w_1600,c_limit/P90429099_lowRes_bmw-ce-04-in-front-o.jpg');

create table orders
(
    id                  int auto_increment
        primary key,
    vehicle_id          int                      not null,
    customer_id         int                      not null,
    status              varchar(50)              null,
    credit              tinyint(1) default 0     not null,
    credit_amount       float                    null,
    credit_rate         double     default 0.05  null,
    credit_currency     varchar(3) default 'EUR' not null,
    card_number         text                     null,
    card_expiry         date                     null,
    card_cvv            int                      null,
    paid_tax            double                   null,
    total_amount        double                   null,
    amount_after_credit double                   null,
    address             text                     null,
    quantity            int        default 1     not null,
    order_date          date                     null,
    constraint orders_ibfk_1
        foreign key (vehicle_id) references vehicles (id),
    constraint orders_ibfk_2
        foreign key (customer_id) references customers (id),
    constraint status_check
        check ((`status` = 'pending') or (`status` = 'validated') or (`status` = 'delivered'))
);

create index vehicle_id
    on orders (vehicle_id);

INSERT INTO java_cars.orders (id, vehicle_id, customer_id, status, credit, credit_amount, credit_rate, credit_currency, card_number, card_expiry, card_cvv, paid_tax, total_amount, amount_after_credit, address, quantity, order_date) VALUES (135, 12, 26, 'validated', 1, 25000, 0.05, 'EUR', '1234567812345678', '2027-11-01', 123, 5000, 55000, 30000, '1 rue de la source, 75016 Paris', 1, '2022-12-08');
INSERT INTO java_cars.orders (id, vehicle_id, customer_id, status, credit, credit_amount, credit_rate, credit_currency, card_number, card_expiry, card_cvv, paid_tax, total_amount, amount_after_credit, address, quantity, order_date) VALUES (136, 6, 26, 'delivered', 0, 0, 0.05, 'EUR', '1234567812345678', '2029-10-01', 123, 1000, 11000, 11000, '1 rue de la source, 75016 Paris', 1, '2022-09-14');
INSERT INTO java_cars.orders (id, vehicle_id, customer_id, status, credit, credit_amount, credit_rate, credit_currency, card_number, card_expiry, card_cvv, paid_tax, total_amount, amount_after_credit, address, quantity, order_date) VALUES (137, 4, 27, 'validated', 0, 0, 0.05, 'EUR', '1234567812345678', '2028-10-01', 123, 2240000, 24640000, 24640000, '1 rue de la route, Tunis, Tunisia', 7, '2022-12-08');
INSERT INTO java_cars.orders (id, vehicle_id, customer_id, status, credit, credit_amount, credit_rate, credit_currency, card_number, card_expiry, card_cvv, paid_tax, total_amount, amount_after_credit, address, quantity, order_date) VALUES (138, 8, 28, 'validated', 1, 3000, 0.05, 'EUR', '1234567812345678', '2027-06-01', 123, 21000, 231000, 228000, '1 rue de quelque part, 75011 Paris', 3, '2022-12-08');
INSERT INTO java_cars.orders (id, vehicle_id, customer_id, status, credit, credit_amount, credit_rate, credit_currency, card_number, card_expiry, card_cvv, paid_tax, total_amount, amount_after_credit, address, quantity, order_date) VALUES (139, 9, 29, 'validated', 1, 2000, 0.05, 'EUR', '1234567812345678', '2023-04-01', 123, 2500, 7500, 5500, '1 avenue du pont, 37000 Tours', 1, '2022-12-08');
INSERT INTO java_cars.orders (id, vehicle_id, customer_id, status, credit, credit_amount, credit_rate, credit_currency, card_number, card_expiry, card_cvv, paid_tax, total_amount, amount_after_credit, address, quantity, order_date) VALUES (140, 10, 30, 'validated', 0, 0, 0.05, 'EUR', '1234567812345678', '2028-11-01', 123, 80, 880, 880, '1 boulevard de la mer, Dakar, Senegal', 2, '2022-12-08');

create table taxes
(
    id           int auto_increment
        primary key,
    country_code varchar(2) charset utf8mb3  not null,
    country_name varchar(80) charset utf8mb3 not null,
    tax          float default 0             not null,
    constraint tax_percentage_check
        check (`tax` <= 1)
)
    collate = utf8mb4_unicode_ci;

INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (1, 'AF', 'Afghanistan', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (2, 'AX', 'Aland Islands', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (3, 'AL', 'Albania', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (4, 'DZ', 'Algeria', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (5, 'AS', 'American Samoa', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (6, 'AD', 'Andorra', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (7, 'AO', 'Angola', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (8, 'AI', 'Anguilla', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (9, 'AQ', 'Antarctica', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (10, 'AG', 'Antigua and Barbuda', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (11, 'AR', 'Argentina', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (12, 'AM', 'Armenia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (13, 'AW', 'Aruba', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (14, 'AU', 'Australia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (15, 'AT', 'Austria', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (16, 'AZ', 'Azerbaijan', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (17, 'BS', 'Bahamas', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (18, 'BH', 'Bahrain', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (19, 'BD', 'Bangladesh', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (20, 'BB', 'Barbados', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (21, 'BY', 'Belarus', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (22, 'BE', 'Belgium', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (23, 'BZ', 'Belize', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (24, 'BJ', 'Benin', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (25, 'BM', 'Bermuda', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (26, 'BT', 'Bhutan', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (27, 'BO', 'Bolivia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (28, 'BQ', 'Bonaire, Sint Eustatius and Saba', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (29, 'BA', 'Bosnia and Herzegovina', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (30, 'BW', 'Botswana', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (31, 'BV', 'Bouvet Island', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (32, 'BR', 'Brazil', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (33, 'IO', 'British Indian Ocean Territory', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (34, 'BN', 'Brunei Darussalam', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (35, 'BG', 'Bulgaria', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (36, 'BF', 'Burkina Faso', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (37, 'BI', 'Burundi', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (38, 'KH', 'Cambodia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (39, 'CM', 'Cameroon', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (40, 'CA', 'Canada', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (41, 'CV', 'Cape Verde', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (42, 'KY', 'Cayman Islands', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (43, 'CF', 'Central African Republic', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (44, 'TD', 'Chad', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (45, 'CL', 'Chile', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (46, 'CN', 'China', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (47, 'CX', 'Christmas Island', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (48, 'CC', 'Cocos (Keeling) Islands', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (49, 'CO', 'Colombia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (50, 'KM', 'Comoros', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (51, 'CG', 'Congo', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (52, 'CD', 'Congo, Democratic Republic of the Congo', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (53, 'CK', 'Cook Islands', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (54, 'CR', 'Costa Rica', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (55, 'CI', 'Cote D''Ivoire', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (56, 'HR', 'Croatia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (57, 'CU', 'Cuba', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (58, 'CW', 'Curacao', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (59, 'CY', 'Cyprus', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (60, 'CZ', 'Czech Republic', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (61, 'DK', 'Denmark', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (62, 'DJ', 'Djibouti', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (63, 'DM', 'Dominica', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (64, 'DO', 'Dominican Republic', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (65, 'EC', 'Ecuador', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (66, 'EG', 'Egypt', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (67, 'SV', 'El Salvador', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (68, 'GQ', 'Equatorial Guinea', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (69, 'ER', 'Eritrea', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (70, 'EE', 'Estonia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (71, 'ET', 'Ethiopia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (72, 'FK', 'Falkland Islands (Malvinas)', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (73, 'FO', 'Faroe Islands', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (74, 'FJ', 'Fiji', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (75, 'FI', 'Finland', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (76, 'FR', 'France', 0.5);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (77, 'GF', 'French Guiana', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (78, 'PF', 'French Polynesia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (79, 'TF', 'French Southern Territories', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (80, 'GA', 'Gabon', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (81, 'GM', 'Gambia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (82, 'GE', 'Georgia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (83, 'DE', 'Germany', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (84, 'GH', 'Ghana', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (85, 'GI', 'Gibraltar', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (86, 'GR', 'Greece', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (87, 'GL', 'Greenland', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (88, 'GD', 'Grenada', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (89, 'GP', 'Guadeloupe', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (90, 'GU', 'Guam', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (91, 'GT', 'Guatemala', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (92, 'GG', 'Guernsey', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (93, 'GN', 'Guinea', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (94, 'GW', 'Guinea-Bissau', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (95, 'GY', 'Guyana', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (96, 'HT', 'Haiti', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (97, 'HM', 'Heard Island and Mcdonald Islands', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (98, 'VA', 'Holy See (Vatican City State)', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (99, 'HN', 'Honduras', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (100, 'HK', 'Hong Kong', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (101, 'HU', 'Hungary', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (102, 'IS', 'Iceland', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (103, 'IN', 'India', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (104, 'ID', 'Indonesia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (105, 'IR', 'Iran, Islamic Republic of', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (106, 'IQ', 'Iraq', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (107, 'IE', 'Ireland', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (108, 'IM', 'Isle of Man', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (109, 'IL', 'Israel', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (110, 'IT', 'Italy', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (111, 'JM', 'Jamaica', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (112, 'JP', 'Japan', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (113, 'JE', 'Jersey', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (114, 'JO', 'Jordan', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (115, 'KZ', 'Kazakhstan', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (116, 'KE', 'Kenya', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (117, 'KI', 'Kiribati', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (118, 'KP', 'Korea, Democratic People''s Republic of', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (119, 'KR', 'Korea, Republic of', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (120, 'XK', 'Kosovo', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (121, 'KW', 'Kuwait', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (122, 'KG', 'Kyrgyzstan', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (123, 'LA', 'Lao People''s Democratic Republic', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (124, 'LV', 'Latvia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (125, 'LB', 'Lebanon', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (126, 'LS', 'Lesotho', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (127, 'LR', 'Liberia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (128, 'LY', 'Libyan Arab Jamahiriya', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (129, 'LI', 'Liechtenstein', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (130, 'LT', 'Lithuania', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (131, 'LU', 'Luxembourg', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (132, 'MO', 'Macao', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (133, 'MK', 'Macedonia, the Former Yugoslav Republic of', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (134, 'MG', 'Madagascar', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (135, 'MW', 'Malawi', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (136, 'MY', 'Malaysia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (137, 'MV', 'Maldives', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (138, 'ML', 'Mali', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (139, 'MT', 'Malta', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (140, 'MH', 'Marshall Islands', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (141, 'MQ', 'Martinique', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (142, 'MR', 'Mauritania', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (143, 'MU', 'Mauritius', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (144, 'YT', 'Mayotte', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (145, 'MX', 'Mexico', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (146, 'FM', 'Micronesia, Federated States of', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (147, 'MD', 'Moldova, Republic of', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (148, 'MC', 'Monaco', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (149, 'MN', 'Mongolia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (150, 'ME', 'Montenegro', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (151, 'MS', 'Montserrat', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (152, 'MA', 'Morocco', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (153, 'MZ', 'Mozambique', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (154, 'MM', 'Myanmar', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (155, 'NA', 'Namibia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (156, 'NR', 'Nauru', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (157, 'NP', 'Nepal', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (158, 'NL', 'Netherlands', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (159, 'AN', 'Netherlands Antilles', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (160, 'NC', 'New Caledonia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (161, 'NZ', 'New Zealand', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (162, 'NI', 'Nicaragua', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (163, 'NE', 'Niger', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (164, 'NG', 'Nigeria', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (165, 'NU', 'Niue', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (166, 'NF', 'Norfolk Island', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (167, 'MP', 'Northern Mariana Islands', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (168, 'NO', 'Norway', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (169, 'OM', 'Oman', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (170, 'PK', 'Pakistan', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (171, 'PW', 'Palau', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (172, 'PS', 'Palestinian Territory, Occupied', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (173, 'PA', 'Panama', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (174, 'PG', 'Papua New Guinea', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (175, 'PY', 'Paraguay', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (176, 'PE', 'Peru', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (177, 'PH', 'Philippines', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (178, 'PN', 'Pitcairn', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (179, 'PL', 'Poland', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (180, 'PT', 'Portugal', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (181, 'PR', 'Puerto Rico', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (182, 'QA', 'Qatar', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (183, 'RE', 'Reunion', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (184, 'RO', 'Romania', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (185, 'RU', 'Russian Federation', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (186, 'RW', 'Rwanda', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (187, 'BL', 'Saint Barthelemy', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (188, 'SH', 'Saint Helena', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (189, 'KN', 'Saint Kitts and Nevis', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (190, 'LC', 'Saint Lucia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (191, 'MF', 'Saint Martin', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (192, 'PM', 'Saint Pierre and Miquelon', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (193, 'VC', 'Saint Vincent and the Grenadines', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (194, 'WS', 'Samoa', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (195, 'SM', 'San Marino', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (196, 'ST', 'Sao Tome and Principe', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (197, 'SA', 'Saudi Arabia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (198, 'SN', 'Senegal', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (199, 'RS', 'Serbia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (200, 'CS', 'Serbia and Montenegro', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (201, 'SC', 'Seychelles', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (202, 'SL', 'Sierra Leone', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (203, 'SG', 'Singapore', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (204, 'SX', 'Sint Maarten', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (205, 'SK', 'Slovakia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (206, 'SI', 'Slovenia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (207, 'SB', 'Solomon Islands', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (208, 'SO', 'Somalia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (209, 'ZA', 'South Africa', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (210, 'GS', 'South Georgia and the South Sandwich Islands', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (211, 'SS', 'South Sudan', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (212, 'ES', 'Spain', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (213, 'LK', 'Sri Lanka', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (214, 'SD', 'Sudan', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (215, 'SR', 'Suriname', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (216, 'SJ', 'Svalbard and Jan Mayen', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (217, 'SZ', 'Swaziland', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (218, 'SE', 'Sweden', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (219, 'CH', 'Switzerland', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (220, 'SY', 'Syrian Arab Republic', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (221, 'TW', 'Taiwan, Province of China', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (222, 'TJ', 'Tajikistan', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (223, 'TZ', 'Tanzania, United Republic of', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (224, 'TH', 'Thailand', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (225, 'TL', 'Timor-Leste', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (226, 'TG', 'Togo', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (227, 'TK', 'Tokelau', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (228, 'TO', 'Tonga', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (229, 'TT', 'Trinidad and Tobago', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (230, 'TN', 'Tunisia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (231, 'TR', 'Turkey', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (232, 'TM', 'Turkmenistan', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (233, 'TC', 'Turks and Caicos Islands', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (234, 'TV', 'Tuvalu', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (235, 'UG', 'Uganda', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (236, 'UA', 'Ukraine', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (237, 'AE', 'United Arab Emirates', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (238, 'GB', 'United Kingdom', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (239, 'US', 'United States', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (240, 'UM', 'United States Minor Outlying Islands', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (241, 'UY', 'Uruguay', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (242, 'UZ', 'Uzbekistan', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (243, 'VU', 'Vanuatu', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (244, 'VE', 'Venezuela', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (245, 'VN', 'Viet Nam', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (246, 'VG', 'Virgin Islands, British', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (247, 'VI', 'Virgin Islands, U.s.', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (248, 'WF', 'Wallis and Futuna', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (249, 'EH', 'Western Sahara', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (250, 'YE', 'Yemen', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (251, 'ZM', 'Zambia', 0.1);
INSERT INTO java_cars.taxes (id, country_code, country_name, tax) VALUES (252, 'ZW', 'Zimbabwe', 0.1);
