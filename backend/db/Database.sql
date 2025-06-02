--
-- PostgreSQL database dump
--

-- Dumped from database version 16.8
-- Dumped by pg_dump version 17.4

-- Started on 2025-04-08 14:09:17

/*SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;*/

--
-- TOC entry 235 (class 1259 OID 16591)
-- Name: answerfc; Type: TABLE; Schema: public; Owner: avnadmin
--
DROP TABLE users CASCADE ;
DROP TABLE testquestion CASCADE ;
DROP TABLE testanswer CASCADE ;
DROP TABLE test CASCADE ;
DROP TABLE streak CASCADE ;
DROP TABLE material CASCADE ;
DROP TABLE grade CASCADE ;
DROP TABLE flashcardsession CASCADE ;
DROP TABLE flashcard CASCADE ;
DROP TABLE enrollment CASCADE ;
DROP TABLE course CASCADE ;
DROP TABLE answerfc CASCADE ;

CREATE TABLE public.answerfc (
    answerid bigint NOT NULL,
    flashcardid bigint NOT NULL,
    optiontext character varying(255) NOT NULL,
    iscorrect boolean NOT NULL
);

--
-- TOC entry 234 (class 1259 OID 16590)
-- Name: answerfc_answerid_seq; Type: SEQUENCE; Schema: public; Owner: avnadmin
--

CREATE SEQUENCE public.answerfc_answerid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 4550 (class 0 OID 0)
-- Dependencies: 234
-- Name: answerfc_answerid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: avnadmin
--

ALTER SEQUENCE public.answerfc_answerid_seq OWNED BY public.answerfc.answerid;


--
-- TOC entry 218 (class 1259 OID 16458)
-- Name: course; Type: TABLE; Schema: public; Owner: avnadmin
--

CREATE TABLE public.course (
    courseid bigint NOT NULL,
    title character varying(255) NOT NULL,
    description character varying(255),
    userid integer NOT NULL,
    semestru character varying(255)
);

--
-- TOC entry 217 (class 1259 OID 16457)
-- Name: course_courseid_seq; Type: SEQUENCE; Schema: public; Owner: avnadmin
--

CREATE SEQUENCE public.course_courseid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 4551 (class 0 OID 0)
-- Dependencies: 217
-- Name: course_courseid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: avnadmin
--

ALTER SEQUENCE public.course_courseid_seq OWNED BY public.course.courseid;


--
-- TOC entry 219 (class 1259 OID 16471)
-- Name: enrollment; Type: TABLE; Schema: public; Owner: avnadmin
--

CREATE TABLE public.enrollment (
    userid bigint NOT NULL,
    courseid bigint NOT NULL,
    enrollmentdate timestamp(6) without time zone NOT NULL,
    grade character varying(255)
);

--
-- TOC entry 227 (class 1259 OID 16534)
-- Name: flashcard; Type: TABLE; Schema: public; Owner: avnadmin
--

CREATE TABLE public.flashcard (
    flashcardid bigint NOT NULL,
    question character varying(255) NOT NULL,
    materialid bigint,
    userid integer NOT NULL,
    level integer,
    laststudiedat timestamp without time zone,
    questiontype character varying(255),
    pageindex integer DEFAULT NULL
);

--
-- TOC entry 226 (class 1259 OID 16533)
-- Name: flashcard_flashcardid_seq; Type: SEQUENCE; Schema: public; Owner: avnadmin
--

CREATE SEQUENCE public.flashcard_flashcardid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 4552 (class 0 OID 0)
-- Dependencies: 226
-- Name: flashcard_flashcardid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: avnadmin
--

ALTER SEQUENCE public.flashcard_flashcardid_seq OWNED BY public.flashcard.flashcardid;


--
-- TOC entry 229 (class 1259 OID 16548)
-- Name: flashcardsession; Type: TABLE; Schema: public; Owner: avnadmin
--

CREATE TABLE public.flashcardsession (
    sessionid bigint NOT NULL,
    userid integer NOT NULL,
    courseid bigint NOT NULL,
    "timestamp" timestamp without time zone NOT NULL,
    flashcardcount integer NOT NULL,
    endtime timestamp(6) without time zone,
    score integer,
    flashcardid bigint
);

--
-- TOC entry 228 (class 1259 OID 16547)
-- Name: flashcardsession_sessionid_seq; Type: SEQUENCE; Schema: public; Owner: avnadmin
--

CREATE SEQUENCE public.flashcardsession_sessionid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 4553 (class 0 OID 0)
-- Dependencies: 228
-- Name: flashcardsession_sessionid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: avnadmin
--

ALTER SEQUENCE public.flashcardsession_sessionid_seq OWNED BY public.flashcardsession.sessionid;


--
-- TOC entry 236 (class 1259 OID 16604)
-- Name: grade; Type: TABLE; Schema: public; Owner: avnadmin
--

CREATE TABLE public.grade (
    testid bigint NOT NULL,
    userid bigint NOT NULL,
    grade double precision NOT NULL,
    submissiondate timestamp without time zone NOT NULL
);

--
-- TOC entry 233 (class 1259 OID 16577)
-- Name: material; Type: TABLE; Schema: public; Owner: avnadmin
--

CREATE TABLE public.material (
    index bigint NOT NULL,
    name character varying(255) NOT NULL,
    courseid bigint NOT NULL,
    path character varying(255) NOT NULL,
    materialid bigint NOT NULL
);

--
-- TOC entry 232 (class 1259 OID 16576)
-- Name: material_index_seq; Type: SEQUENCE; Schema: public; Owner: avnadmin
--

CREATE SEQUENCE public.material_index_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 4554 (class 0 OID 0)
-- Dependencies: 232
-- Name: material_index_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: avnadmin
--

ALTER SEQUENCE public.material_index_seq OWNED BY public.material.index;


--
-- TOC entry 238 (class 1259 OID 16938)
-- Name: material_materialid_seq; Type: SEQUENCE; Schema: public; Owner: avnadmin
--

ALTER TABLE public.material ALTER COLUMN materialid ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.material_materialid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 231 (class 1259 OID 16565)
-- Name: streak; Type: TABLE; Schema: public; Owner: avnadmin
--

CREATE TABLE public.streak (
    streakid bigint NOT NULL,
    userid integer NOT NULL,
    currentstreak integer NOT NULL,
    lastcompleteddate date NOT NULL
);

--
-- TOC entry 230 (class 1259 OID 16564)
-- Name: streak_streakid_seq; Type: SEQUENCE; Schema: public; Owner: avnadmin
--

CREATE SEQUENCE public.streak_streakid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 4555 (class 0 OID 0)
-- Dependencies: 230
-- Name: streak_streakid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: avnadmin
--

ALTER SEQUENCE public.streak_streakid_seq OWNED BY public.streak.streakid;


--
-- TOC entry 221 (class 1259 OID 16487)
-- Name: test; Type: TABLE; Schema: public; Owner: avnadmin
--

CREATE TABLE public.test (
    testid bigint NOT NULL,
    title character varying(255) NOT NULL,
    description character varying(255),
    date timestamp(6) without time zone NOT NULL,
    userid integer NOT NULL,
    courseid bigint NOT NULL
);

--
-- TOC entry 220 (class 1259 OID 16486)
-- Name: test_testid_seq; Type: SEQUENCE; Schema: public; Owner: avnadmin
--

CREATE SEQUENCE public.test_testid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 4556 (class 0 OID 0)
-- Dependencies: 220
-- Name: test_testid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: avnadmin
--

ALTER SEQUENCE public.test_testid_seq OWNED BY public.test.testid;


--
-- TOC entry 225 (class 1259 OID 16520)
-- Name: testanswer; Type: TABLE; Schema: public; Owner: avnadmin
--

CREATE TABLE public.testanswer (
    answerid bigint NOT NULL,
    questionid bigint NOT NULL,
    iscorrect boolean NOT NULL,
    optiontext character varying(255) NOT NULL
);

--
-- TOC entry 224 (class 1259 OID 16519)
-- Name: testanswer_answerid_seq; Type: SEQUENCE; Schema: public; Owner: avnadmin
--

CREATE SEQUENCE public.testanswer_answerid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 4557 (class 0 OID 0)
-- Dependencies: 224
-- Name: testanswer_answerid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: avnadmin
--

ALTER SEQUENCE public.testanswer_answerid_seq OWNED BY public.testanswer.answerid;


--
-- TOC entry 223 (class 1259 OID 16506)
-- Name: testquestion; Type: TABLE; Schema: public; Owner: avnadmin
--

CREATE TABLE public.testquestion (
    questionid bigint NOT NULL,
    testid bigint NOT NULL,
    questiontext character varying(255) NOT NULL,
    pointvalue double precision NOT NULL,
    answerid bigint NOT NULL
);

--
-- TOC entry 237 (class 1259 OID 16847)
-- Name: testquestion_answerid_seq; Type: SEQUENCE; Schema: public; Owner: avnadmin
--

ALTER TABLE public.testquestion ALTER COLUMN answerid ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.testquestion_answerid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 222 (class 1259 OID 16505)
-- Name: testquestion_questionid_seq; Type: SEQUENCE; Schema: public; Owner: avnadmin
--

CREATE SEQUENCE public.testquestion_questionid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 4558 (class 0 OID 0)
-- Dependencies: 222
-- Name: testquestion_questionid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: avnadmin
--

ALTER SEQUENCE public.testquestion_questionid_seq OWNED BY public.testquestion.questionid;


--
-- TOC entry 216 (class 1259 OID 16446)
-- Name: users; Type: TABLE; Schema: public; Owner: avnadmin
--

CREATE TABLE public.users (
    userid integer NOT NULL,
    firstname character varying(255) NOT NULL,
    lastname character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    role character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    CONSTRAINT users_role_check CHECK (((role)::text = ANY (ARRAY[('student'::character varying)::text, ('profesor'::character varying)::text, ('admin'::character varying)::text])))
);

--
-- TOC entry 215 (class 1259 OID 16445)
-- Name: users_userid_seq; Type: SEQUENCE; Schema: public; Owner: avnadmin
--

CREATE SEQUENCE public.users_userid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- TOC entry 4559 (class 0 OID 0)
-- Dependencies: 215
-- Name: users_userid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: avnadmin
--

ALTER SEQUENCE public.users_userid_seq OWNED BY public.users.userid;


--
-- TOC entry 4332 (class 2604 OID 16619)
-- Name: answerfc answerid; Type: DEFAULT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.answerfc ALTER COLUMN answerid SET DEFAULT nextval('public.answerfc_answerid_seq'::regclass);


--
-- TOC entry 4324 (class 2604 OID 16641)
-- Name: course courseid; Type: DEFAULT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.course ALTER COLUMN courseid SET DEFAULT nextval('public.course_courseid_seq'::regclass);


--
-- TOC entry 4328 (class 2604 OID 16708)
-- Name: flashcard flashcardid; Type: DEFAULT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.flashcard ALTER COLUMN flashcardid SET DEFAULT nextval('public.flashcard_flashcardid_seq'::regclass);


--
-- TOC entry 4329 (class 2604 OID 16734)
-- Name: flashcardsession sessionid; Type: DEFAULT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.flashcardsession ALTER COLUMN sessionid SET DEFAULT nextval('public.flashcardsession_sessionid_seq'::regclass);


--
-- TOC entry 4331 (class 2604 OID 16772)
-- Name: material index; Type: DEFAULT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.material ALTER COLUMN index SET DEFAULT nextval('public.material_index_seq'::regclass);


--
-- TOC entry 4330 (class 2604 OID 16798)
-- Name: streak streakid; Type: DEFAULT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.streak ALTER COLUMN streakid SET DEFAULT nextval('public.streak_streakid_seq'::regclass);


--
-- TOC entry 4325 (class 2604 OID 16805)
-- Name: test testid; Type: DEFAULT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.test ALTER COLUMN testid SET DEFAULT nextval('public.test_testid_seq'::regclass);


--
-- TOC entry 4327 (class 2604 OID 16916)
-- Name: testanswer answerid; Type: DEFAULT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.testanswer ALTER COLUMN answerid SET DEFAULT nextval('public.testanswer_answerid_seq'::regclass);


--
-- TOC entry 4326 (class 2604 OID 16854)
-- Name: testquestion questionid; Type: DEFAULT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.testquestion ALTER COLUMN questionid SET DEFAULT nextval('public.testquestion_questionid_seq'::regclass);


--
-- TOC entry 4323 (class 2604 OID 16449)
-- Name: users userid; Type: DEFAULT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.users ALTER COLUMN userid SET DEFAULT nextval('public.users_userid_seq'::regclass);


--
-- TOC entry 4541 (class 0 OID 16591)
-- Dependencies: 235
-- Data for Name: answerfc; Type: TABLE DATA; Schema: public; Owner: avnadmin
--



--
-- TOC entry 4524 (class 0 OID 16458)
-- Dependencies: 218
-- Data for Name: course; Type: TABLE DATA; Schema: public; Owner: avnadmin
--

INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (21, 'Algoritmica Grafurilor', 'Curs despre algoritmi pe grafuri.', 66, '3');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (22, 'Arhitectura Calculatoarelor și Sisteme de Operare', 'Structura și funcționarea sistemelor de operare.', 7, '1');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (23, 'Baze de Date', 'Modelarea și interogarea bazelor de date.', 63, '3');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (24, 'Calcul Numeric', 'Metode numerice pentru rezolvarea problemelor matematice.', 2, '1');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (25, 'Fundamentele Algebrice ale Informaticii', 'Bazele algebrice ale logicii și informaticii.', 5, '2');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (26, 'Grafică pe Calculator', 'Tehnici de afișare și procesare a graficii.', 7, '6');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (27, 'Ingineria Programării', 'Metodologii de dezvoltare software.', 63, '4');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (28, 'Inteligența Artificială', 'Introducere în AI, agenți inteligenți.', 71, '5');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (29, 'Învățare Automată', 'Algoritmi de machine learning.', 7, '5');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (30, 'Limbaje Formale, Automate și Compilatoare', 'Teoria limbajelor și automatizări.', 69, '3');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (31, 'Logică pentru Informatică', 'Logică propozițională și predicate.', 72, '1');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (32, 'Probabilități și Statistică', 'Bazele probabilităților și statisticii.', 66, '2');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (33, 'Programare Avansată', 'Programare procedurală și funcțională avansată.', 65, '4');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (34, 'Programare Orientată-Obiect', 'Paradigma OOP în Java și C++.', 5, '2');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (35, 'Proiectarea Algoritmilor', 'Strategii eficiente de proiectare algoritmică.', 64, '2');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (36, 'Rețele de Calculatoare', 'Protocoale, modele OSI și TCP/IP/UDP.', 70, '3');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (37, 'Securitatea Informației', 'Principii și tehnici de securitate.', 72, '4');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (38, 'Sisteme de Operare', 'Gestionarea proceselor și resurselor.', 2, '2');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (39, 'Structuri de Date', 'Liste, arbori, grafuri și complexitate.', 64, '1');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (40, 'Tehnologii Web', 'HTML, CSS, JavaScript și backend.', 72, '4');


--
-- TOC entry 4525 (class 0 OID 16471)
-- Dependencies: 219
-- Data for Name: enrollment; Type: TABLE DATA; Schema: public; Owner: avnadmin
--

INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (11, 22, '2024-11-05 00:00:00', '6.75');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (11, 24, '2024-12-18 00:00:00', '8.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (11, 26, '2025-01-10 00:00:00', '9.40');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (11, 33, '2024-10-28 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (12, 23, '2024-10-17 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (12, 28, '2024-12-04 00:00:00', '7.25');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (12, 31, '2024-12-20 00:00:00', '9.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (12, 37, '2025-01-29 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (13, 24, '2024-11-23 00:00:00', '6.50');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (13, 26, '2025-01-05 00:00:00', '8.60');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (13, 34, '2025-02-11 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (13, 39, '2024-12-15 00:00:00', '9.75');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (14, 21, '2024-10-03 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (14, 27, '2024-11-30 00:00:00', '7.90');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (14, 35, '2025-02-22 00:00:00', '8.20');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (15, 22, '2024-10-12 00:00:00', '5.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (15, 25, '2024-11-17 00:00:00', '6.60');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (15, 38, '2024-12-28 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (15, 40, '2025-01-15 00:00:00', '9.90');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (16, 23, '2024-10-25 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (16, 29, '2024-11-08 00:00:00', '7.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (16, 33, '2025-01-22 00:00:00', '8.45');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (16, 36, '2024-12-11 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (17, 26, '2024-10-10 00:00:00', '7.80');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (17, 30, '2024-11-27 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (17, 34, '2025-01-09 00:00:00', '8.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (17, 39, '2025-02-25 00:00:00', '9.30');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (18, 21, '2024-11-03 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (18, 28, '2024-12-14 00:00:00', '6.95');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (18, 31, '2025-01-18 00:00:00', '8.70');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (19, 22, '2024-10-05 00:00:00', '6.25');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (19, 27, '2024-11-20 00:00:00', '7.55');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (19, 32, '2025-02-03 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (19, 37, '2025-02-27 00:00:00', '9.10');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (20, 24, '2024-11-01 00:00:00', '7.10');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (20, 26, '2024-12-06 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (20, 30, '2025-01-13 00:00:00', '9.85');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (21, 23, '2024-10-08 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (21, 25, '2024-11-25 00:00:00', '7.75');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (21, 35, '2025-01-31 00:00:00', '8.55');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (21, 38, '2025-02-10 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (22, 21, '2024-10-11 00:00:00', '6.90');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (22, 28, '2024-11-29 00:00:00', '9.10');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (22, 33, '2025-01-07 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (23, 22, '2024-12-03 00:00:00', '8.60');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (23, 27, '2024-12-21 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (23, 36, '2025-02-05 00:00:00', '7.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (24, 23, '2024-11-13 00:00:00', '5.95');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (24, 26, '2025-01-01 00:00:00', '8.90');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (24, 29, '2025-02-17 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (24, 40, '2025-02-28 00:00:00', '9.25');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (25, 24, '2024-11-10 00:00:00', '8.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (25, 25, '2024-12-08 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (25, 31, '2025-01-19 00:00:00', '6.85');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (26, 22, '2024-10-15 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (26, 28, '2024-11-22 00:00:00', '9.50');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (26, 37, '2025-02-07 00:00:00', '7.80');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (27, 21, '2024-11-07 00:00:00', '7.25');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (27, 34, '2025-01-26 00:00:00', '8.10');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (27, 39, '2025-02-12 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (28, 23, '2024-10-13 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (28, 27, '2024-12-10 00:00:00', '8.95');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (28, 32, '2025-01-15 00:00:00', '9.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (29, 24, '2024-10-30 00:00:00', '6.70');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (29, 33, '2024-12-30 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (29, 35, '2025-02-02 00:00:00', '8.40');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (30, 25, '2024-11-19 00:00:00', '7.60');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (30, 29, '2024-12-16 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (30, 38, '2025-02-19 00:00:00', '9.60');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (31, 26, '2024-10-09 00:00:00', '8.20');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (31, 30, '2024-11-28 00:00:00', '7.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (31, 36, '2025-02-08 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (32, 28, '2024-12-12 00:00:00', '9.80');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (32, 31, '2025-01-30 00:00:00', '8.25');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (32, 40, '2025-02-23 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (33, 22, '2024-10-06 00:00:00', '6.45');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (33, 27, '2024-11-24 00:00:00', '7.35');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (33, 34, '2025-02-14 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (35, 21, '2024-11-05 00:00:00', '8.45');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (35, 23, '2024-12-03 00:00:00', '7.90');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (35, 26, '2025-01-12 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (35, 31, '2025-02-19 00:00:00', '9.30');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (36, 22, '2024-11-15 00:00:00', '7.50');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (36, 27, '2025-01-17 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (36, 33, '2025-02-10 00:00:00', '8.75');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (37, 25, '2024-10-18 00:00:00', '6.80');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (37, 28, '2024-12-11 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (37, 30, '2025-01-25 00:00:00', '8.20');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (37, 35, '2025-02-22 00:00:00', '9.10');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (38, 26, '2024-10-30 00:00:00', '7.25');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (38, 29, '2024-11-29 00:00:00', '8.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (38, 32, '2025-01-14 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (38, 36, '2025-02-18 00:00:00', '9.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (39, 27, '2024-11-10 00:00:00', '6.50');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (39, 30, '2024-12-05 00:00:00', '7.85');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (39, 33, '2025-01-20 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (39, 37, '2025-02-28 00:00:00', '9.45');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (40, 21, '2024-10-22 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (40, 25, '2024-11-12 00:00:00', '7.40');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (40, 28, '2025-01-05 00:00:00', '8.90');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (40, 34, '2025-02-13 00:00:00', '9.60');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (41, 22, '2024-11-01 00:00:00', '8.15');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (41, 29, '2024-12-14 00:00:00', '6.75');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (41, 32, '2025-01-09 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (41, 38, '2025-02-20 00:00:00', '7.95');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (42, 23, '2024-10-12 00:00:00', '7.35');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (42, 26, '2024-11-22 00:00:00', '9.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (42, 31, '2025-01-19 00:00:00', '8.40');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (42, 40, '2025-02-23 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (43, 24, '2024-11-03 00:00:00', '8.65');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (44, 25, '2024-10-28 00:00:00', '7.50');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (44, 28, '2024-11-18 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (44, 33, '2025-01-30 00:00:00', '8.10');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (45, 32, '2025-01-12 00:00:00', '9.40');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (45, 34, '2025-02-25 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (46, 21, '2024-11-06 00:00:00', '6.80');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (46, 24, '2024-12-04 00:00:00', '7.40');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (46, 29, '2025-01-10 00:00:00', '8.55');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (46, 36, '2025-02-17 00:00:00', '8.95');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (47, 23, '2024-11-18 00:00:00', '7.25');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (47, 25, '2024-12-09 00:00:00', '8.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (47, 30, '2025-01-25 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (47, 37, '2025-02-12 00:00:00', '9.10');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (48, 22, '2024-10-21 00:00:00', '7.65');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (48, 27, '2024-11-27 00:00:00', '6.95');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (48, 34, '2025-01-16 00:00:00', '8.30');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (48, 38, '2025-02-04 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (49, 21, '2024-10-10 00:00:00', '8.10');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (49, 29, '2024-12-01 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (49, 33, '2025-01-22 00:00:00', '9.50');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (50, 22, '2024-11-09 00:00:00', '8.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (50, 30, '2024-12-13 00:00:00', '7.55');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (50, 32, '2025-01-03 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (50, 40, '2025-02-11 00:00:00', '9.15');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (51, 24, '2024-10-27 00:00:00', '6.95');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (51, 29, '2024-11-24 00:00:00', '7.80');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (51, 36, '2025-01-08 00:00:00', '8.40');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (51, 39, '2025-02-02 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (52, 25, '2024-11-13 00:00:00', '7.10');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (52, 31, '2024-12-02 00:00:00', '8.20');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (52, 34, '2025-01-14 00:00:00', '9.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (52, 37, '2025-02-24 00:00:00', '9.45');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (53, 21, '2024-11-04 00:00:00', '8.75');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (53, 26, '2024-12-06 00:00:00', '7.30');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (53, 32, '2025-01-18 00:00:00', '9.60');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (53, 39, '2025-02-15 00:00:00', NULL);
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (54, 23, '2024-10-19 00:00:00', '7.50');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (54, 28, '2024-11-10 00:00:00', '8.10');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (54, 35, '2025-01-22 00:00:00', '8.30');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (54, 38, '2025-02-13 00:00:00', '9.20');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (55, 21, '2024-10-12 00:00:00', '8.25');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (55, 24, '2024-11-05 00:00:00', '6.90');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (55, 32, '2025-01-03 00:00:00', '8.80');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (56, 27, '2024-11-14 00:00:00', '7.40');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (56, 33, '2025-01-07 00:00:00', '8.25');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (56, 39, '2025-02-18 00:00:00', '9.10');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (57, 38, '2025-02-03 00:00:00', '9.35');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (58, 24, '2024-10-16 00:00:00', '7.80');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (58, 29, '2024-11-11 00:00:00', '9.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (58, 33, '2025-01-15 00:00:00', '8.15');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (59, 28, '2024-11-03 00:00:00', '7.20');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (59, 30, '2025-01-11 00:00:00', '8.50');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (60, 23, '2024-11-04 00:00:00', '7.90');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (60, 25, '2024-12-02 00:00:00', '8.00');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (60, 32, '2025-01-13 00:00:00', '9.20');
INSERT INTO public.enrollment (userid, courseid, enrollmentdate, grade) VALUES (60, 34, '2025-02-14 00:00:00', NULL);


--
-- TOC entry 4533 (class 0 OID 16534)
-- Dependencies: 227
-- Data for Name: flashcard; Type: TABLE DATA; Schema: public; Owner: avnadmin
--

INSERT INTO public.flashcard (flashcardid, question, materialid, userid, level, laststudiedat, questiontype) VALUES (6, 'Cum funcționează algoritmul de sortare QuickSort?', 2, 22, 2, '2024-03-12 12:30:00', 'Exercițiu');
INSERT INTO public.flashcard (flashcardid, question, materialid, userid, level, laststudiedat, questiontype) VALUES (7, 'Ce reprezintă termenul de Big O în analiza algoritmilor?', 3, 31, 3, '2024-03-15 15:45:00', 'Teorie');
INSERT INTO public.flashcard (flashcardid, question, materialid, userid, level, laststudiedat, questiontype) VALUES (8, 'Explicați diferența între stive și cozi?', 4, 13, 4, '2024-03-18 09:00:00', 'Exercițiu');
INSERT INTO public.flashcard (flashcardid, question, materialid, userid, level, laststudiedat, questiontype) VALUES (5, 'Care este complexitatea algoritmului Dijkstra?', 1, 10, 2, '2024-03-10 10:00:00', 'Teorie');

-- Update existing flashcard records with pageindex values
UPDATE public.flashcard SET pageindex = 5 WHERE flashcardid = 5;
UPDATE public.flashcard SET pageindex = 12 WHERE flashcardid = 6;
UPDATE public.flashcard SET pageindex = 7 WHERE flashcardid = 7;
UPDATE public.flashcard SET pageindex = 3 WHERE flashcardid = 8;

-- Insert additional flashcard with pageindex
INSERT INTO public.flashcard (flashcardid, question, materialid, userid, level, laststudiedat, questiontype, pageindex) 
VALUES (9, 'Explicați principiile algoritmilor DFS și BFS pentru parcurgerea grafurilor', 10, 14, 3, '2024-04-02 08:15:00', 'Teorie', 15);


--
-- TOC entry 4535 (class 0 OID 16548)
-- Dependencies: 229
-- Data for Name: flashcardsession; Type: TABLE DATA; Schema: public; Owner: avnadmin
--

INSERT INTO public.flashcardsession (sessionid, userid, courseid, "timestamp", flashcardcount, endtime, score, flashcardid) VALUES (5, 10, 21, '2024-03-10 09:30:00', 5, NULL, NULL, NULL);
INSERT INTO public.flashcardsession (sessionid, userid, courseid, "timestamp", flashcardcount, endtime, score, flashcardid) VALUES (6, 22, 39, '2024-03-12 10:00:00', 8, NULL, NULL, NULL);
INSERT INTO public.flashcardsession (sessionid, userid, courseid, "timestamp", flashcardcount, endtime, score, flashcardid) VALUES (7, 31, 39, '2024-03-15 11:30:00', 6, NULL, NULL, NULL);
INSERT INTO public.flashcardsession (sessionid, userid, courseid, "timestamp", flashcardcount, endtime, score, flashcardid) VALUES (8, 13, 28, '2024-03-18 08:45:00', 4, NULL, NULL, NULL);


--
-- TOC entry 4542 (class 0 OID 16604)
-- Dependencies: 236
-- Data for Name: grade; Type: TABLE DATA; Schema: public; Owner: avnadmin
--

INSERT INTO public.grade (testid, userid, grade, submissiondate) VALUES (1, 10, 8.75, '2024-03-10 10:30:00');
INSERT INTO public.grade (testid, userid, grade, submissiondate) VALUES (1, 22, 9, '2024-03-12 11:00:00');
INSERT INTO public.grade (testid, userid, grade, submissiondate) VALUES (2, 31, 7.5, '2024-03-15 16:00:00');
INSERT INTO public.grade (testid, userid, grade, submissiondate) VALUES (3, 13, 6.8, '2024-03-18 09:30:00');


--
-- TOC entry 4539 (class 0 OID 16577)
-- Dependencies: 233
-- Data for Name: material; Type: TABLE DATA; Schema: public; Owner: avnadmin
--

INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (1, 'Teorie algoritmi Dijkstra', 21, '/path/to/material/algoritmi_dijkstra.pdf', 1);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (2, 'Algoritm QuickSort', 39, '/path/to/material/quicksort.pdf', 2);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (3, 'Big O Notation', 39, '/path/to/material/big_o_notation.pdf', 3);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (4, 'Stive și Cozi', 39, '/path/to/material/stive_cozi.pdf', 4);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (5, 'Introducer arbori', 21, 'https://example.com/material/arbori.pdf', 5);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (6, 'Căutarea binară', 33, 'https://example.com/material/cautare_binara.pdf', 6);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (7, 'Linii și Coloane', 26, 'https://example.com/material/structuri_date.pdf', 7);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (8, 'Algoritmi dinamici', 35, 'https://example.com/material/algoritmi_dinamici.pdf', 8);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (9, 'Recursivitate în programare', 30, 'https://example.com/material/recursivitate.pdf', 9);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (10, 'Introducere în grafuri', 21, 'https://example.com/material/introducere_grafuri.pdf', 10);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (11, 'Cicluri și arbori de acoperire', 21, 'https://example.com/material/cicluri_arbori_acoperire.pdf', 11);


--
-- TOC entry 4537 (class 0 OID 16565)
-- Dependencies: 231
-- Data for Name: streak; Type: TABLE DATA; Schema: public; Owner: avnadmin
--

INSERT INTO public.streak (streakid, userid, currentstreak, lastcompleteddate) VALUES (1, 10, 3, '2024-03-10');
INSERT INTO public.streak (streakid, userid, currentstreak, lastcompleteddate) VALUES (2, 11, 5, '2024-03-12');
INSERT INTO public.streak (streakid, userid, currentstreak, lastcompleteddate) VALUES (3, 12, 2, '2024-03-15');
INSERT INTO public.streak (streakid, userid, currentstreak, lastcompleteddate) VALUES (4, 13, 4, '2024-03-18');


--
-- TOC entry 4527 (class 0 OID 16487)
-- Dependencies: 221
-- Data for Name: test; Type: TABLE DATA; Schema: public; Owner: avnadmin
--

INSERT INTO public.test (testid, title, description, date, userid, courseid) VALUES (1, 'Test 1', 'Test pentru Algoritmica Grafurilor', '2025-04-10 00:00:00', 66, 21);
INSERT INTO public.test (testid, title, description, date, userid, courseid) VALUES (2, 'Test 2', 'Test pentru Baze de Date', '2025-04-15 00:00:00', 63, 23);
INSERT INTO public.test (testid, title, description, date, userid, courseid) VALUES (3, 'Test 3', 'Test pentru Programare Avansată', '2025-04-20 00:00:00', 65, 33);


--
-- TOC entry 4531 (class 0 OID 16520)
-- Dependencies: 225
-- Data for Name: testanswer; Type: TABLE DATA; Schema: public; Owner: avnadmin
--

INSERT INTO public.testanswer (answerid, questionid, iscorrect, optiontext) VALUES (15, 4, false, 'O(n)');
INSERT INTO public.testanswer (answerid, questionid, iscorrect, optiontext) VALUES (16, 4, true, 'O(n log n)');
INSERT INTO public.testanswer (answerid, questionid, iscorrect, optiontext) VALUES (17, 4, false, 'O(1)');
INSERT INTO public.testanswer (answerid, questionid, iscorrect, optiontext) VALUES (18, 4, false, 'O(n^2)');
INSERT INTO public.testanswer (answerid, questionid, iscorrect, optiontext) VALUES (19, 5, false, 'Un graf neorienta conex');
INSERT INTO public.testanswer (answerid, questionid, iscorrect, optiontext) VALUES (20, 5, true, 'Un arbore de acoperire minimă conectează toate nodurile unui graf');
INSERT INTO public.testanswer (answerid, questionid, iscorrect, optiontext) VALUES (21, 6, false, 'O bază de date relațională este mai flexibilă decât una NoSQL');
INSERT INTO public.testanswer (answerid, questionid, iscorrect, optiontext) VALUES (22, 6, true, 'O bază de date NoSQL este mai scalabilă decât una relațională');
INSERT INTO public.testanswer (answerid, questionid, iscorrect, optiontext) VALUES (23, 7, true, 'Normalizarea bazei de date se referă la reducerea redundanței');
INSERT INTO public.testanswer (answerid, questionid, iscorrect, optiontext) VALUES (24, 7, false, 'Normalizarea bazei de date înseamnă adăugarea de date suplimentare pentru a îmbunătăți performanța');
INSERT INTO public.testanswer (answerid, questionid, iscorrect, optiontext) VALUES (25, 8, true, 'Tehnicile de programare orientată pe obiecte sunt folosite pentru a organiza datele');
INSERT INTO public.testanswer (answerid, questionid, iscorrect, optiontext) VALUES (26, 8, false, 'Programarea orientată pe obiecte este folosită doar pentru aplicații grafice');
INSERT INTO public.testanswer (answerid, questionid, iscorrect, optiontext) VALUES (27, 9, false, 'Polimorfismul static presupune decizia legării funcțiilor în timpul execuției');
INSERT INTO public.testanswer (answerid, questionid, iscorrect, optiontext) VALUES (28, 9, true, 'Polimorfismul static se referă la legarea funcțiilor în timpul compilării');


--
-- TOC entry 4529 (class 0 OID 16506)
-- Dependencies: 223
-- Data for Name: testquestion; Type: TABLE DATA; Schema: public; Owner: avnadmin
--

INSERT INTO public.testquestion (questionid, testid, questiontext, pointvalue, answerid) VALUES (4, 1, 'Care este complexitatea algoritmului Dijkstra?', 2, 4);
INSERT INTO public.testquestion (questionid, testid, questiontext, pointvalue, answerid) VALUES (5, 1, 'Ce este un arbore de acoperire minimă?', 3, 5);
INSERT INTO public.testquestion (questionid, testid, questiontext, pointvalue, answerid) VALUES (6, 2, 'Care este diferența dintre o bază de date relațională și una NoSQL?', 2.5, 6);
INSERT INTO public.testquestion (questionid, testid, questiontext, pointvalue, answerid) VALUES (7, 2, 'Ce înseamnă normalizarea unei baze de date?', 2.5, 7);
INSERT INTO public.testquestion (questionid, testid, questiontext, pointvalue, answerid) VALUES (8, 3, 'Care este scopul tehnicilor de programare orientată pe obiecte?', 3, 8);
INSERT INTO public.testquestion (questionid, testid, questiontext, pointvalue, answerid) VALUES (9, 3, 'Explică diferența dintre polimorfismul static și dinamic.', 2.5, 9);


--
-- TOC entry 4522 (class 0 OID 16446)
-- Dependencies: 216
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: avnadmin
--

INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (1, 'John', 'Doe', 'john.doe@example.com', 'student', 'hashed_password_1');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (2, 'Jane', 'Smith', 'jane.smith@example.com', 'profesor', 'hashed_password_2');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (3, 'Alice', 'Brown', 'alice.brown@example.com', 'admin', 'hashed_password_3');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (4, 'Bob', 'Johnson', 'bob.johnson@example.com', 'student', 'hashed_password_4');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (5, 'Emily', 'Davis', 'emily.davis@example.com', 'profesor', 'hashed_password_5');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (6, 'Andrei', 'Popescu', 'andrei@student.com', 'student', 'parola123');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (7, 'Ioana', 'Marinescu', 'ioana@profesor.com', 'profesor', 'parola456');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (8, 'Mihai', 'Radu', 'mihai@admin.com', 'admin', 'adminpass');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (10, 'Maria', 'Ionescu', 'maria.ionescu@student.com', 'student', 'z7Lp3Xq!');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (11, 'Vlad', 'Georgescu', 'vlad.georgescu@student.com', 'student', 'Qw92rTY@');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (12, 'Ana', 'Dumitru', 'ana.dumitru@student.com', 'student', 'mN4#vT8e');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (13, 'Radu', 'Enache', 'radu.enache@student.com', 'student', 'Yt3!pLo9');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (14, 'Ioana', 'Costache', 'ioana.costache@student.com', 'student', 'Ke1@zXw5');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (15, 'Cristian', 'Tudor', 'cristian.tudor@student.com', 'student', '9rT@eL6w');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (16, 'Elena', 'Popa', 'elena.popa@student.com', 'student', 'Px2$Mn7q');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (17, 'Daria', 'Marin', 'daria.marin@student.com', 'student', 'Jk8&vB3!');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (18, 'Alex', 'Dobre', 'alex.dobre@student.com', 'student', 'Xv2!aNp7');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (19, 'Bianca', 'Stan', 'bianca.stan@student.com', 'student', 'Gq9$Lm3z');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (20, 'Florin', 'Matei', 'florin.matei@student.com', 'student', 'Uz4#Rt2k');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (21, 'Teodora', 'Ilie', 'teodora.ilie@student.com', 'student', 'Mv6@Xp9q');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (22, 'Andreea', 'Voicu', 'andreea.voicu@student.com', 'student', 'Jt7!Pw8e');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (23, 'Marius', 'Serban', 'marius.serban@student.com', 'student', 'Rb3^Yo6l');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (24, 'Catalin', 'Petrescu', 'catalin.petrescu@student.com', 'student', 'Np5*Zx2q');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (25, 'Gabriela', 'Nistor', 'gabriela.nistor@student.com', 'student', 'Lk8@Rn5w');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (26, 'Ionut', 'Mihaila', 'ionut.mihaila@student.com', 'student', 'Vd3!Tp7z');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (27, 'Roxana', 'Ciobanu', 'roxana.ciobanu@student.com', 'student', 'Qe6#Uy4p');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (28, 'Diana', 'Barbu', 'diana.barbu@student.com', 'student', 'Wm2@Jq9x');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (29, 'George', 'Preda', 'george.preda@student.com', 'student', 'Ef9$Kr6b');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (30, 'Alina', 'Toma', 'alina.toma@student.com', 'student', 'Tf1^Xp8m');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (31, 'Paul', 'Nedelea', 'paul.nedelea@student.com', 'student', 'Yr4!Bm3v');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (32, 'Raluca', 'Stefan', 'raluca.stefan@student.com', 'student', 'Zo7@Wt5q');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (33, 'Sorin', 'Manole', 'sorin.manole@student.com', 'student', 'Hm5#Lz9k');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (34, 'Carmen', 'Olteanu', 'carmen.olteanu@student.com', 'student', 'Pn6!Xv2e');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (35, 'Dan', 'Voinea', 'dan.voinea@student.com', 'student', 'Jx8@Wc4r');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (36, 'Simona', 'Dinu', 'simona.dinu@student.com', 'student', 'Bt3#Mq7s');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (37, 'Ovidiu', 'Rusu', 'ovidiu.rusu@student.com', 'student', 'Ke2!Zn8l');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (38, 'Cristiano', 'Ronaldo', 'Ronaldo_goat@gmail.com', 'student', 'pessi');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (39, 'Ionel', 'Messi', 'Messiuuuu@gmail.com', 'student', 'Georgina');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (40, 'Ilinca', 'Badea', 'ilinca.badea@student.com', 'student', 'Wx4!Tu9m');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (41, 'Lucian', 'Zamfir', 'lucian.zamfir@student.com', 'student', 'Ky7@Zn3e');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (42, 'Natalia', 'Popescu', 'natalia.popescu@student.com', 'student', 'Qs9#Pt4l');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (43, 'Andrei', 'Voiculescu', 'andrei.voiculescu@student.com', 'student', 'Df3^Jm8r');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (44, 'Camelia', 'Frunza', 'camelia.frunza@student.com', 'student', 'Rz6@Nx5v');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (45, 'Ciprian', 'Ignat', 'ciprian.ignat@student.com', 'student', 'Mg1!Uz6p');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (46, 'Silvia', 'Balan', 'silvia.balan@student.com', 'student', 'Tv8$Ky2m');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (47, 'Sebastian', 'Fodor', 'sebastian.fodor@student.com', 'student', 'Xn5#Lo3t');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (48, 'Ioan', 'Gavrila', 'ioan.gavrila@student.com', 'student', 'Vz2^Hr9k');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (49, 'Oana', 'Calin', 'oana.calin@student.com', 'student', 'Jk6!Mt7w');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (50, 'Denisa', 'Baciu', 'denisa.baciu@student.com', 'student', 'Fg3@Yv5q');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (51, 'Rares', 'Grigore', 'rares.grigore@student.com', 'student', 'Lp7#Qu8z');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (52, 'Larisa', 'Tanase', 'larisa.tanase@student.com', 'student', 'Qm9!Wz6n');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (53, 'Dragoș', 'Popa', 'dragos.popa@student.com', 'student', 'Uz4^Kt2b');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (54, 'Anca', 'Neagu', 'anca.neagu@student.com', 'student', 'Ep6!Zj3x');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (55, 'Mihnea', 'Paraschiv', 'mihnea.paraschiv@student.com', 'student', 'Xv2@Nt6e');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (56, 'Sorina', 'Lungu', 'sorina.lungu@student.com', 'student', 'Tf8$Mw7q');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (57, 'Adrian', 'Barca', 'adrian.barca@student.com', 'student', 'Yq1!Vr5z');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (58, 'Corina', 'Andrei', 'corina.andrei@student.com', 'student', 'Lz5#Tp9v');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (59, 'Tudor', 'Marinescu', 'tudor.marinescu@student.com', 'student', 'Mk6@Yc3p');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (60, 'Ileana', 'Stroe', 'ileana.stroe@student.com', 'student', 'Kf2$Rz8x');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (61, 'Victor', 'Mocanu', 'victor.mocanu@student.com', 'student', 'Wm7^Jp4s');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (62, 'Daniela', 'Cojocaru', 'daniela.cojocaru@student.com', 'student', 'Gn3!Lx6r');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (63, 'Adrian', 'Iftenie', 'adrian.iftenie@profesor.com', 'profesor', 'Pf9@Yu3r');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (64, 'Alexandru', 'Ionita', 'alexandru.ionita@profesor.com', 'profesor', 'Vt3#Lk7p');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (65, 'Gabriel', 'Constantinescu', 'gabriel.constantinescu@profesor.com', 'profesor', 'Mn6!Qw4z');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (66, 'Florin', 'Olariu', 'florin.olariu@profesor.com', 'profesor', 'Rz5@Xp9v');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (67, 'Mihai', 'Dinca', 'mihai.dinca@profesor.com', 'profesor', 'Tk2!Vm7q');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (68, 'Andreea', 'Nistor', 'andreea.nistor@profesor.com', 'profesor', 'Jx7$Qr8m');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (69, 'Cristina', 'Lazar', 'cristina.lazar@profesor.com', 'profesor', 'Wm4#Uy2e');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (70, 'Radu', 'Iliescu', 'radu.iliescu@profesor.com', 'profesor', 'Ep3!Vn5k');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (71, 'Murinho', 'special_one', 'pressure000@profesor.com', 'profesor', 'Ht6@Zp3l');
INSERT INTO public.users (userid, firstname, lastname, email, role, password) VALUES (72, 'El', 'Professor', 'lacasadepappel@profesor.com', 'profesor', 'Yq9$Tr6x');

-- Enable the pgcrypto extension if not already enabled
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Update the users table to encode passwords using the crypt() function
UPDATE public.users
SET password = crypt(password, gen_salt('bf'));

-- Note: Use the following Java code for password validation:
-- boolean passwordMatches = passwordEncoder.matches(rawPassword, user.getPassword());


--
-- TOC entry 4560 (class 0 OID 0)
-- Dependencies: 234
-- Name: answerfc_answerid_seq; Type: SEQUENCE SET; Schema: public; Owner: avnadmin
--

SELECT pg_catalog.setval('public.answerfc_answerid_seq', 8, true);


--
-- TOC entry 4561 (class 0 OID 0)
-- Dependencies: 217
-- Name: course_courseid_seq; Type: SEQUENCE SET; Schema: public; Owner: avnadmin
--

SELECT pg_catalog.setval('public.course_courseid_seq', 40, true);


--
-- TOC entry 4562 (class 0 OID 0)
-- Dependencies: 226
-- Name: flashcard_flashcardid_seq; Type: SEQUENCE SET; Schema: public; Owner: avnadmin
--

SELECT pg_catalog.setval('public.flashcard_flashcardid_seq', 8, true);


--
-- TOC entry 4563 (class 0 OID 0)
-- Dependencies: 228
-- Name: flashcardsession_sessionid_seq; Type: SEQUENCE SET; Schema: public; Owner: avnadmin
--

SELECT pg_catalog.setval('public.flashcardsession_sessionid_seq', 8, true);


--
-- TOC entry 4564 (class 0 OID 0)
-- Dependencies: 232
-- Name: material_index_seq; Type: SEQUENCE SET; Schema: public; Owner: avnadmin
--

SELECT pg_catalog.setval('public.material_index_seq', 11, true);


--
-- TOC entry 4565 (class 0 OID 0)
-- Dependencies: 238
-- Name: material_materialid_seq; Type: SEQUENCE SET; Schema: public; Owner: avnadmin
--

SELECT pg_catalog.setval('public.material_materialid_seq', 11, true);


--
-- TOC entry 4566 (class 0 OID 0)
-- Dependencies: 230
-- Name: streak_streakid_seq; Type: SEQUENCE SET; Schema: public; Owner: avnadmin
--

SELECT pg_catalog.setval('public.streak_streakid_seq', 4, true);


--
-- TOC entry 4567 (class 0 OID 0)
-- Dependencies: 220
-- Name: test_testid_seq; Type: SEQUENCE SET; Schema: public; Owner: avnadmin
--

SELECT pg_catalog.setval('public.test_testid_seq', 3, true);


--
-- TOC entry 4568 (class 0 OID 0)
-- Dependencies: 224
-- Name: testanswer_answerid_seq; Type: SEQUENCE SET; Schema: public; Owner: avnadmin
--

SELECT pg_catalog.setval('public.testanswer_answerid_seq', 28, true);


--
-- TOC entry 4569 (class 0 OID 0)
-- Dependencies: 237
-- Name: testquestion_answerid_seq; Type: SEQUENCE SET; Schema: public; Owner: avnadmin
--

SELECT pg_catalog.setval('public.testquestion_answerid_seq', 9, true);


--
-- TOC entry 4570 (class 0 OID 0)
-- Dependencies: 222
-- Name: testquestion_questionid_seq; Type: SEQUENCE SET; Schema: public; Owner: avnadmin
--

SELECT pg_catalog.setval('public.testquestion_questionid_seq', 9, true);


--
-- TOC entry 4571 (class 0 OID 0)
-- Dependencies: 215
-- Name: users_userid_seq; Type: SEQUENCE SET; Schema: public; Owner: avnadmin
--

SELECT pg_catalog.setval('public.users_userid_seq', 72, true);


--
-- TOC entry 4357 (class 2606 OID 16621)
-- Name: answerfc answerfc_pkey; Type: CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.answerfc
    ADD CONSTRAINT answerfc_pkey PRIMARY KEY (answerid);


--
-- TOC entry 4339 (class 2606 OID 16643)
-- Name: course course_pkey; Type: CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (courseid);


--
-- TOC entry 4341 (class 2606 OID 16698)
-- Name: enrollment enrollment_pkey; Type: CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.enrollment
    ADD CONSTRAINT enrollment_pkey PRIMARY KEY (userid, courseid);


--
-- TOC entry 4349 (class 2606 OID 16710)
-- Name: flashcard flashcard_pkey; Type: CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.flashcard
    ADD CONSTRAINT flashcard_pkey PRIMARY KEY (flashcardid);


--
-- TOC entry 4351 (class 2606 OID 16736)
-- Name: flashcardsession flashcardsession_pkey; Type: CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.flashcardsession
    ADD CONSTRAINT flashcardsession_pkey PRIMARY KEY (sessionid);


--
-- TOC entry 4359 (class 2606 OID 16762)
-- Name: grade grade_pkey; Type: CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.grade
    ADD CONSTRAINT grade_pkey PRIMARY KEY (testid, userid);


--
-- TOC entry 4355 (class 2606 OID 16774)
-- Name: material material_pkey; Type: CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.material
    ADD CONSTRAINT material_pkey PRIMARY KEY (index);


--
-- TOC entry 4353 (class 2606 OID 16800)
-- Name: streak streak_pkey; Type: CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.streak
    ADD CONSTRAINT streak_pkey PRIMARY KEY (streakid);


--
-- TOC entry 4343 (class 2606 OID 16807)
-- Name: test test_pkey; Type: CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.test
    ADD CONSTRAINT test_pkey PRIMARY KEY (testid);


--
-- TOC entry 4347 (class 2606 OID 16918)
-- Name: testanswer testanswer_pkey; Type: CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.testanswer
    ADD CONSTRAINT testanswer_pkey PRIMARY KEY (answerid);


--
-- TOC entry 4345 (class 2606 OID 16856)
-- Name: testquestion testquestion_pkey; Type: CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.testquestion
    ADD CONSTRAINT testquestion_pkey PRIMARY KEY (questionid);


--
-- TOC entry 4335 (class 2606 OID 16456)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 4337 (class 2606 OID 16454)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (userid);


--
-- TOC entry 4375 (class 2606 OID 16711)
-- Name: answerfc answerfc_flashcardid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.answerfc
    ADD CONSTRAINT answerfc_flashcardid_fkey FOREIGN KEY (flashcardid) REFERENCES public.flashcard(flashcardid) ON DELETE CASCADE;


--
-- TOC entry 4360 (class 2606 OID 16466)
-- Name: course course_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(userid) ON DELETE CASCADE;


--
-- TOC entry 4361 (class 2606 OID 16688)
-- Name: enrollment enrollment_courseid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.enrollment
    ADD CONSTRAINT enrollment_courseid_fkey FOREIGN KEY (courseid) REFERENCES public.course(courseid) ON DELETE CASCADE;


--
-- TOC entry 4362 (class 2606 OID 16699)
-- Name: enrollment enrollment_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.enrollment
    ADD CONSTRAINT enrollment_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(userid) ON DELETE CASCADE;


--
-- TOC entry 4370 (class 2606 OID 16945)
-- Name: flashcardsession fk2a9to8w64cp2c6nxl6an15hek; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.flashcardsession
    ADD CONSTRAINT fk2a9to8w64cp2c6nxl6an15hek FOREIGN KEY (flashcardid) REFERENCES public.flashcard(flashcardid);


--
-- TOC entry 4368 (class 2606 OID 16893)
-- Name: flashcard fk87hmwh0f1cur5kscdddt777sw; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.flashcard
    ADD CONSTRAINT fk87hmwh0f1cur5kscdddt777sw FOREIGN KEY (materialid) REFERENCES public.material(index);


--
-- TOC entry 4365 (class 2606 OID 16898)
-- Name: testquestion fkfvkfjourtdwrr61q4hwsrkj0h; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.testquestion
    ADD CONSTRAINT fkfvkfjourtdwrr61q4hwsrkj0h FOREIGN KEY (questionid) REFERENCES public.testquestion(questionid);


--
-- TOC entry 4369 (class 2606 OID 16542)
-- Name: flashcard flashcard_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.flashcard
    ADD CONSTRAINT flashcard_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(userid) ON DELETE CASCADE;


--
-- TOC entry 4371 (class 2606 OID 16741)
-- Name: flashcardsession flashcardsession_courseid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.flashcardsession
    ADD CONSTRAINT flashcardsession_courseid_fkey FOREIGN KEY (courseid) REFERENCES public.course(courseid) ON DELETE CASCADE;


--
-- TOC entry 4372 (class 2606 OID 16554)
-- Name: flashcardsession flashcardsession_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.flashcardsession
    ADD CONSTRAINT flashcardsession_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(userid) ON DELETE CASCADE;


--
-- TOC entry 4376 (class 2606 OID 16813)
-- Name: grade grade_testid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.grade
    ADD CONSTRAINT grade_testid_fkey FOREIGN KEY (testid) REFERENCES public.test(testid) ON DELETE CASCADE;


--
-- TOC entry 4377 (class 2606 OID 16763)
-- Name: grade grade_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.grade
    ADD CONSTRAINT grade_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(userid) ON DELETE CASCADE;


--
-- TOC entry 4374 (class 2606 OID 16787)
-- Name: material material_courseid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.material
    ADD CONSTRAINT material_courseid_fkey FOREIGN KEY (courseid) REFERENCES public.course(courseid) ON DELETE CASCADE;


--
-- TOC entry 4373 (class 2606 OID 16571)
-- Name: streak streak_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.streak
    ADD CONSTRAINT streak_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(userid) ON DELETE CASCADE;


--
-- TOC entry 4363 (class 2606 OID 16836)
-- Name: test test_courseid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.test
    ADD CONSTRAINT test_courseid_fkey FOREIGN KEY (courseid) REFERENCES public.course(courseid) ON DELETE CASCADE;


--
-- TOC entry 4364 (class 2606 OID 16495)
-- Name: test test_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.test
    ADD CONSTRAINT test_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(userid) ON DELETE CASCADE;


--
-- TOC entry 4367 (class 2606 OID 16929)
-- Name: testanswer testanswer_questionid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.testanswer
    ADD CONSTRAINT testanswer_questionid_fkey FOREIGN KEY (questionid) REFERENCES public.testquestion(questionid) ON DELETE CASCADE;


--
-- TOC entry 4366 (class 2606 OID 16874)
-- Name: testquestion testquestion_testid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: avnadmin
--

ALTER TABLE ONLY public.testquestion
    ADD CONSTRAINT testquestion_testid_fkey FOREIGN KEY (testid) REFERENCES public.test(testid) ON DELETE CASCADE;


-- Completed on 2025-04-08 14:09:23

--
-- PostgreSQL database dump complete
--

