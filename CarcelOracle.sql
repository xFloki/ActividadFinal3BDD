
CREATE OR REPLACE TYPE t_delito AS OBJECT (
  codigo_delito NUMBER(11),
  descripcion VARCHAR2(500),
  condena NUMBER(11)
);

/

CREATE TABLE delito OF t_delito

/

CREATE OR REPLACE TYPE telefono AS OBJECT (tipo VARCHAR2(15),
Num_telefono VARCHAR2(20));

/

CREATE TYPE tabla_telefonos AS TABLE OF telefono;

/

CREATE OR REPLACE TYPE t_preso AS OBJECT(
  codigo_preso VARCHAR2(56),
  dni VARCHAR2(11) ,
  nombre VARCHAR2(125) ,
  apellidos VARCHAR2(254) ,
  fecha_nacimiento DATE,
  lugar_nacimiento VARCHAR2(56),
  nacionalidad VARCHAR2(56),
  estatura NUMBER(3),
  color_piel VARCHAR2(11),
  banda VARCHAR2(98) ,
  telefono tabla_telefonos,
  fotoPreso VARCHAR2(69) 
); 

/

CREATE TABLE preso OF t_preso
NESTED TABLE telefono STORE AS nt_telefono;

/

CREATE OR REPLACE TYPE t_preso_delito AS OBJECT (
  codigo_preso REF t_preso,
  codigo_delito REF t_delito,
  circustancias VARCHAR2(500) ,
  ubicacion VARCHAR2(55) 
); 

/

CREATE TABLE preso_delito OF t_preso_delito

/

INSERT ALL
  INTO delito VALUES
  (11, 'Conducci?n bajo los efectos del alcohol', 2)
  INTO delito VALUES
  (44, 'Alteration del Orden publico', 2)
  INTO delito VALUES
  (55, 'Cyber Acoso', 5)
  INTO delito VALUES
  (88, 'Violation a un adulto', 6)
  INTO delito VALUES
  (99, 'Atraco a mano armada', 5)
  SELECT * FROM dual;

/

INSERT ALL 
  INTO preso VALUES
  ('0443', '0994', 'Bertin', 'Scaziolo', to_date('02-01-1979','DD-MM-YYYY'), 'Desconocida', 'dsa', 3, 'Negra', 'Nieta',tabla_telefonos(telefono('Movil', '658956332'), telefono('Abogado','918273645')), '/imagenes/paccino.jpeg')
   INTO preso VALUES
  ('2000', '0987', 'Nico', 'Nicolas', to_date('01-11-2016','DD-MM-YYYY'), 'Madrid', 'Española', 2, 'Blanca', 'Latin King',tabla_telefonos( telefono('Abogado','918273645')), '/imagenes/jimmy.jpg')
   INTO preso VALUES
  ('2201', '0566', 'Julian', 'Volt', to_date('02-11-1988','DD-MM-YYYY'), 'España', 'Español', 2, 'Blanca', 'Nieta', tabla_telefonos(telefono('Abogado','444')), '/imagenes/jimmy.jpg')
   INTO preso VALUES
  ('2207', '5330', 'Juanito', 'Escobar',to_date('16-11-2016','DD-MM-YYYY') , 'Panama', 'Española', 2, 'Azul', 'Nieta',tabla_telefonos(telefono('Movil', '658956332'), telefono('Abogado','918273645')), '/imagenes/paccino.jpeg')
   INTO preso VALUES
  ('2206', '0765', 'Adam', 'Rubio', to_date('10-08-1999','DD-MM-YYYY'), 'España', 'Africana', 2, 'Azul', 'Sumare',tabla_telefonos(telefono('Familiar', '333'), telefono('Abogado','918273645')), '/imagenes/jimmy.jpg')
   INTO preso VALUES
  ('2222', '0777', 'Cantero', 'Canteru', to_date('09-05-1988','DD-MM-YYYY'), 'Nigeria', 'Noruega', 0, 'Blanca', 'Hackers',tabla_telefonos( telefono('Abogado','918273645')), '/imagenes/paccino.jpeg')
    SELECT * FROM dual;

/
INSERT  ALL
  INTO preso_delito VALUES
  ((SELECT REF(pres) FROM preso pres where pres.codigo_preso ='2206'), 
    (SELECT REF(del) FROM delito del where del.codigo_delito = 55),  
  'Se desconocen las causas que le llevaron ', 'Pozuelo')
  INTO preso_delito VALUES
  ((SELECT REF(pres) FROM preso pres where pres.codigo_preso ='2222'), 
    (SELECT REF(del) FROM delito del where del.codigo_delito = 55),  
  'Se desconocen las causas que le llevaron ', 'Madrid')
   INTO preso_delito VALUES
  ((SELECT REF(pres) FROM preso pres where pres.codigo_preso ='2206'), 
    (SELECT REF(del) FROM delito del where del.codigo_delito = 88),  
  'Estaba muy alterado por examenes finales', 'Universidad Francisco de Vitoria')
   INTO preso_delito VALUES
  ((SELECT REF(pres) FROM preso pres where pres.codigo_preso ='2206'), 
    (SELECT REF(del) FROM delito del where del.codigo_delito = 99),  
  'Se desconocen las causas que le llevaron ', 'Universidad Complutense de Madrid')
  INTO preso_delito VALUES
  ((SELECT REF(pres) FROM preso pres where pres.codigo_preso ='0443'), 
    (SELECT REF(del) FROM delito del where del.codigo_delito = 99),  
  'Se desconocen las causas que le llevaron ', 'Universidad Complutense de Madrid')
  INTO preso_delito VALUES
  ((SELECT REF(pres) FROM preso pres where pres.codigo_preso ='2201'), 
    (SELECT REF(del) FROM delito del where del.codigo_delito = 99),  
  'Se desconocen las causas que le llevaron ', 'Universidad Complutense de Madrid')
  INTO preso_delito VALUES ((SELECT REF(pres) FROM preso pres where pres.codigo_preso ='2000'), 
    (SELECT REF(del) FROM delito del where del.codigo_delito = 99),  
  'Se desconocen las causas que le llevaron ', 'Universidad Complutense de Madrid')
  SELECT * FROM dual;
  
  
  
  /*

Select c.codigo_preso.codigo_preso, c.codigo_delito.codigo_delito, c.circustancias, c.ubicacion from preso_delito c where c.codigo_preso = (SELECT REF(pres) FROM preso pres where pres.codigo_preso ='2000');

INSERT INTO preso_delito VALUES ((SELECT REF(pres) FROM preso pres where pres.codigo_preso ='2000'),(SELECT REF(del) FROM delito del where del.codigo_delito = 99));
INSERT INTO preso_delito VALUES ((SELECT REF(pres) FROM preso pres where pres.codigo_preso ='"2201"'),(SELECT REF(del) FROM delito del where del.codigo_delito = 88),'cxzczxc', 'czxcxzc');

Select * from preso where banda = 'Nieta';
Select * from preso where nombre = 'Juanito';
  Select * from preso where banda = 'Nieta'and nombre = 'Juanito';

select  tt.telefono from preso p, table(p."Abogado") tt;

Insert INTO preso VALUES  ('333', '0994', 'Bertin', 'Scaziolo', to_date('02-01-1979','DD-MM-YYYY'),'Desconocida', 'dsa', 3, 'Negra', 'Nieta', tabla_telefonos(), '/imagenes/paccino.jpeg') ; 

Insert INTO preso VALUES  ('333', '0994', 'Bertin', 'Scaziolo', to_date('02-01-1979','DD-MM-YYYY'),'Desconocida', 'dsa', 3, 'Negra', 'Nieta', tabla_telefonos(), '/imagenes/paccino.jpeg') ; 
 
Insert INTO preso VALUES ('fdsf', 'fsdf', 'fsd', 'fsd', to_date('12-11-1992','DD-MM-YYYY'), 'dsda', 'dsad', 2, 'sdad', 'dasd' ,  tabla_telefonos(telefono('Movil','444'),telefono('Movil','444')), '/imagenes/paccino.jpeg' );

Insert INTO preso VALUES  ('sdijnijnj', 'fdsfs', 'eeeee', 'eeee', to_date('12-11-2000','DD-MM-YYYY'), 'rrrr', 'rrrr', 2, 'rrr', 'rrrr' , tabla_telefonos(telefono('Movil','444'),telefono('Movil','444')) , '/imagenes/paccino.jpeg');


select p.dni,p.nombre,p.apellidos, t.tipo, t.Num_telefono from preso p, TABLE(p.telefono) t;

select CONCAT(concat(t.tipo,'     '), t.Num_telefono) from preso p, table(p.telefono) t where p.codigo_preso = '0443' and t.tipo='Abogado';

UPDATE preso SET dni = '0442' WHERE preso.codigo_preso = '2206';
UPDATE preso SET fecha_nacimiento = '16-11-1993' WHERE preso.codigo_preso = '2000';

update table(select telefono from preso where codigo_preso = '2206' )set Num_telefono = '666721354' where tipo = 'Abogado';

insert into table ( select telefono from preso where  codigo_preso = '2206' )  values ( 'Familiar' , '555' );
  
select * from preso;



DROP TABLE delito;
DROP TABLE preso;
DROP TABLE preso_delito;

DROP type t_delito FORCE;
DROP type t_preso FORCE;
DROP type t_preso_delito FORCE;

*/


