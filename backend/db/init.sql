--
-- PostgreSQL database dump
--

-- Dumped from database version 16.8
-- Dumped by pg_dump version 17.4

--SET statement_timeout = 0;
--SET lock_timeout = 0;
--SET idle_in_transaction_session_timeout = 0;
--SET transaction_timeout = 0;
--SET client_encoding = 'UTF8';
--SET standard_conforming_strings = on;
--SELECT pg_catalog.set_config('search_path', '', false);
--SET check_function_bodies = false;
--SET xmloption = content;
--SET client_min_messages = warning;
--SET row_security = off;

--SET default_tablespace = '';

--SET default_table_access_method = heap;

--
-- Name: answerfc; Type: TABLE; Schema: public; Owner: -
--

DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS testquestion CASCADE;
DROP TABLE IF EXISTS testanswer CASCADE;
DROP TABLE IF EXISTS test CASCADE;
DROP TABLE IF EXISTS streak CASCADE;
DROP TABLE IF EXISTS material CASCADE;
DROP TABLE IF EXISTS grade CASCADE;
DROP TABLE IF EXISTS flashcardsession CASCADE;
DROP TABLE IF EXISTS flashcard CASCADE;
DROP TABLE IF EXISTS enrollment CASCADE;
DROP TABLE IF EXISTS course CASCADE;
DROP TABLE IF EXISTS answerfc CASCADE;


CREATE TABLE public.answerfc (
    answerid bigint NOT NULL,
    flashcardid bigint NOT NULL,
    optiontext character varying(255) NOT NULL,
    iscorrect boolean NOT NULL
);


--
-- Name: answerfc_answerid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.answerfc_answerid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: answerfc_answerid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.answerfc_answerid_seq OWNED BY public.answerfc.answerid;


--
-- Name: course; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.course (
    courseid bigint NOT NULL,
    title character varying(255) NOT NULL,
    description character varying(255),
    userid integer NOT NULL,
    semestru character varying(255)
);


--
-- Name: course_courseid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.course_courseid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: course_courseid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.course_courseid_seq OWNED BY public.course.courseid;


--
-- Name: enrollment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.enrollment (
    userid bigint NOT NULL,
    courseid bigint NOT NULL,
    enrollmentdate timestamp(6) without time zone NOT NULL,
    grade character varying(255)
);


--
-- Name: flashcard; Type: TABLE; Schema: public; Owner: -
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
-- Name: flashcard_flashcardid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.flashcard_flashcardid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: flashcard_flashcardid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.flashcard_flashcardid_seq OWNED BY public.flashcard.flashcardid;


--
-- Name: flashcardsession; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.flashcardsession (
    sessionid bigint NOT NULL,
    userid bigint NOT NULL,
    courseid bigint NOT NULL,
    timestamp timestamp without time zone NOT NULL,
    flashcardcount integer NOT NULL
);


--
-- Name: flashcardsession_sessionid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.flashcardsession_sessionid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: flashcardsession_sessionid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.flashcardsession_sessionid_seq OWNED BY public.flashcardsession.sessionid;


--
-- Name: grade; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.grade (
    testid bigint NOT NULL,
    userid bigint NOT NULL,
    grade double precision NOT NULL,
    submissiondate timestamp without time zone NOT NULL
);


--
-- Name: material; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.material (
    index bigint NOT NULL,
    name character varying(255) NOT NULL,
    courseid bigint NOT NULL,
    path character varying(255) NOT NULL,
    materialid bigint NOT NULL
);


--
-- Name: material_index_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.material_index_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: material_index_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.material_index_seq OWNED BY public.material.index;


--
-- Name: material_materialid_seq; Type: SEQUENCE; Schema: public; Owner: -
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
-- Name: streak; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.streak (
    streakid bigint NOT NULL,
    userid integer NOT NULL,
    currentstreak integer NOT NULL,
    lastcompleteddate date NOT NULL
);


--
-- Name: streak_streakid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.streak_streakid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: streak_streakid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.streak_streakid_seq OWNED BY public.streak.streakid;


--
-- Name: test; Type: TABLE; Schema: public; Owner: -
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
-- Name: test_testid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.test_testid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: test_testid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.test_testid_seq OWNED BY public.test.testid;


--
-- Name: testanswer; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.testanswer (
    answerid bigint NOT NULL,
    questionid bigint NOT NULL,
    iscorrect boolean NOT NULL,
    optiontext character varying(255) NOT NULL
);


--
-- Name: testanswer_answerid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.testanswer_answerid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: testanswer_answerid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.testanswer_answerid_seq OWNED BY public.testanswer.answerid;


--
-- Name: testquestion; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.testquestion (
    questionid bigint NOT NULL,
    testid bigint NOT NULL,
    questiontext character varying(255) NOT NULL,
    pointvalue double precision NOT NULL,
    answerid bigint NOT NULL
);


--
-- Name: testquestion_answerid_seq; Type: SEQUENCE; Schema: public; Owner: -
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
-- Name: testquestion_questionid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.testquestion_questionid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: testquestion_questionid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.testquestion_questionid_seq OWNED BY public.testquestion.questionid;


--
-- Name: users; Type: TABLE; Schema: public; Owner: -
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
-- Name: users_userid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.users_userid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: users_userid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.users_userid_seq OWNED BY public.users.userid;


--
-- Name: answerfc answerid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.answerfc ALTER COLUMN answerid SET DEFAULT nextval('public.answerfc_answerid_seq'::regclass);


--
-- Name: course courseid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.course ALTER COLUMN courseid SET DEFAULT nextval('public.course_courseid_seq'::regclass);


--
-- Name: flashcard flashcardid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.flashcard ALTER COLUMN flashcardid SET DEFAULT nextval('public.flashcard_flashcardid_seq'::regclass);


--
-- Name: flashcardsession sessionid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.flashcardsession ALTER COLUMN sessionid SET DEFAULT nextval('public.flashcardsession_sessionid_seq'::regclass);


--
-- Name: material index; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.material ALTER COLUMN index SET DEFAULT nextval('public.material_index_seq'::regclass);


--
-- Name: streak streakid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.streak ALTER COLUMN streakid SET DEFAULT nextval('public.streak_streakid_seq'::regclass);


--
-- Name: test testid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.test ALTER COLUMN testid SET DEFAULT nextval('public.test_testid_seq'::regclass);


--
-- Name: testanswer answerid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.testanswer ALTER COLUMN answerid SET DEFAULT nextval('public.testanswer_answerid_seq'::regclass);


--
-- Name: testquestion questionid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.testquestion ALTER COLUMN questionid SET DEFAULT nextval('public.testquestion_questionid_seq'::regclass);


--
-- Name: users userid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users ALTER COLUMN userid SET DEFAULT nextval('public.users_userid_seq'::regclass);


--
-- Data for Name: answerfc; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- Data for Name: course; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (21, 'Algoritmica_Grafurilor', 'Curs despre algoritmi pe grafuri.', 66, '3');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (22, 'Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare', 'Structura și funcționarea sistemelor de operare.', 7, '1');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (23, 'Baze_De_Date', 'Modelarea și interogarea bazelor de date.', 63, '3');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (24, 'Calcul_Numeric', 'Metode numerice pentru rezolvarea problemelor matematice.', 2, '1');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (25, 'Fundamentele_Algebrice_Ale_Informaticii', 'Bazele algebrice ale logicii și informaticii.', 5, '2');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (26, 'Grafica_Pe_Calculator_Si_Geometrie_Computationala', 'Tehnici de afișare și procesare a graficii.', 7, '6');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (27, 'Ingineria_Programarii', 'Metodologii de dezvoltare software.', 63, '4');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (28, 'Inteligenta_Artificiala', 'Introducere în AI, agenți inteligenți.', 71, '5');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (29, 'Invatare_Automata', 'Algoritmi de machine learning.', 7, '5');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (30, 'Limbaje_Formale_Automate_Si_Compilatoare', 'Teoria limbajelor și automatizări.', 69, '3');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (31, 'Logica_Pentru_Informatica', 'Logică propozițională și predicate.', 72, '1');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (41, 'Matematica_Calcul_Diferential_Si_Integral', 'math', 72, '1');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (42, 'Practica_Sisteme_De_Gestiune_Pentru_Baze_De_Date', 'bd', 72, '4');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (32, 'Probabilitati_Si_Statistica', 'Bazele probabilităților și statisticii.', 66, '2');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (33, 'Programare_Avansata', 'Programare procedurală și funcțională avansată.', 65, '4');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (34, 'Programare_Orientata_Obiect', 'Paradigma OOP în Java și C++.', 5, '2');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (43, 'Programare_Python', 'serpi', 5, '5');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (44, 'Programare_Rust', 'rugina', 5, '3');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (35, 'Proiectarea_Algoritmilor', 'Strategii eficiente de proiectare algoritmică.', 64, '2');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (36, 'Retele_De_Calculatoare', 'Protocoale, modele OSI și TCP/IP/UDP.', 70, '3');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (37, 'Securitatea_Informatiei', 'Principii și tehnici de securitate.', 72, '4');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (38, 'Sisteme_De_Operare', 'Gestionarea proceselor și resurselor.', 2, '2');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (39, 'Structuri_De_Date', 'Liste, arbori, grafuri și complexitate.', 64, '1');
INSERT INTO public.course (courseid, title, description, userid, semestru) VALUES (40, 'Tehnologii_WEB', 'HTML, CSS, JavaScript și backend.', 72, '4');

--
-- Data for Name: enrollment; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.enrollment VALUES (11, 22, '2024-11-05 00:00:00', '6.75');
INSERT INTO public.enrollment VALUES (11, 24, '2024-12-18 00:00:00', '8.00');
INSERT INTO public.enrollment VALUES (11, 26, '2025-01-10 00:00:00', '9.40');
INSERT INTO public.enrollment VALUES (11, 33, '2024-10-28 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (12, 23, '2024-10-17 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (12, 28, '2024-12-04 00:00:00', '7.25');
INSERT INTO public.enrollment VALUES (12, 31, '2024-12-20 00:00:00', '9.00');
INSERT INTO public.enrollment VALUES (12, 37, '2025-01-29 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (13, 24, '2024-11-23 00:00:00', '6.50');
INSERT INTO public.enrollment VALUES (13, 26, '2025-01-05 00:00:00', '8.60');
INSERT INTO public.enrollment VALUES (13, 34, '2025-02-11 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (13, 39, '2024-12-15 00:00:00', '9.75');
INSERT INTO public.enrollment VALUES (14, 21, '2024-10-03 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (14, 27, '2024-11-30 00:00:00', '7.90');
INSERT INTO public.enrollment VALUES (14, 35, '2025-02-22 00:00:00', '8.20');
INSERT INTO public.enrollment VALUES (15, 22, '2024-10-12 00:00:00', '5.00');
INSERT INTO public.enrollment VALUES (15, 25, '2024-11-17 00:00:00', '6.60');
INSERT INTO public.enrollment VALUES (15, 38, '2024-12-28 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (15, 40, '2025-01-15 00:00:00', '9.90');
INSERT INTO public.enrollment VALUES (16, 23, '2024-10-25 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (16, 29, '2024-11-08 00:00:00', '7.00');
INSERT INTO public.enrollment VALUES (16, 33, '2025-01-22 00:00:00', '8.45');
INSERT INTO public.enrollment VALUES (16, 36, '2024-12-11 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (17, 26, '2024-10-10 00:00:00', '7.80');
INSERT INTO public.enrollment VALUES (17, 30, '2024-11-27 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (17, 34, '2025-01-09 00:00:00', '8.00');
INSERT INTO public.enrollment VALUES (17, 39, '2025-02-25 00:00:00', '9.30');
INSERT INTO public.enrollment VALUES (18, 21, '2024-11-03 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (18, 28, '2024-12-14 00:00:00', '6.95');
INSERT INTO public.enrollment VALUES (18, 31, '2025-01-18 00:00:00', '8.70');
INSERT INTO public.enrollment VALUES (19, 22, '2024-10-05 00:00:00', '6.25');
INSERT INTO public.enrollment VALUES (19, 27, '2024-11-20 00:00:00', '7.55');
INSERT INTO public.enrollment VALUES (19, 32, '2025-02-03 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (19, 37, '2025-02-27 00:00:00', '9.10');
INSERT INTO public.enrollment VALUES (20, 24, '2024-11-01 00:00:00', '7.10');
INSERT INTO public.enrollment VALUES (20, 26, '2024-12-06 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (20, 30, '2025-01-13 00:00:00', '9.85');
INSERT INTO public.enrollment VALUES (21, 23, '2024-10-08 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (21, 25, '2024-11-25 00:00:00', '7.75');
INSERT INTO public.enrollment VALUES (21, 35, '2025-01-31 00:00:00', '8.55');
INSERT INTO public.enrollment VALUES (21, 38, '2025-02-10 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (22, 21, '2024-10-11 00:00:00', '6.90');
INSERT INTO public.enrollment VALUES (22, 28, '2024-11-29 00:00:00', '9.10');
INSERT INTO public.enrollment VALUES (22, 33, '2025-01-07 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (23, 22, '2024-12-03 00:00:00', '8.60');
INSERT INTO public.enrollment VALUES (23, 27, '2024-12-21 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (23, 36, '2025-02-05 00:00:00', '7.00');
INSERT INTO public.enrollment VALUES (24, 23, '2024-11-13 00:00:00', '5.95');
INSERT INTO public.enrollment VALUES (24, 26, '2025-01-01 00:00:00', '8.90');
INSERT INTO public.enrollment VALUES (24, 29, '2025-02-17 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (24, 40, '2025-02-28 00:00:00', '9.25');
INSERT INTO public.enrollment VALUES (25, 24, '2024-11-10 00:00:00', '8.00');
INSERT INTO public.enrollment VALUES (25, 25, '2024-12-08 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (25, 31, '2025-01-19 00:00:00', '6.85');
INSERT INTO public.enrollment VALUES (26, 22, '2024-10-15 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (26, 28, '2024-11-22 00:00:00', '9.50');
INSERT INTO public.enrollment VALUES (26, 37, '2025-02-07 00:00:00', '7.80');
INSERT INTO public.enrollment VALUES (27, 21, '2024-11-07 00:00:00', '7.25');
INSERT INTO public.enrollment VALUES (27, 34, '2025-01-26 00:00:00', '8.10');
INSERT INTO public.enrollment VALUES (27, 39, '2025-02-12 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (28, 23, '2024-10-13 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (28, 27, '2024-12-10 00:00:00', '8.95');
INSERT INTO public.enrollment VALUES (28, 32, '2025-01-15 00:00:00', '9.00');
INSERT INTO public.enrollment VALUES (29, 24, '2024-10-30 00:00:00', '6.70');
INSERT INTO public.enrollment VALUES (29, 33, '2024-12-30 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (29, 35, '2025-02-02 00:00:00', '8.40');
INSERT INTO public.enrollment VALUES (30, 25, '2024-11-19 00:00:00', '7.60');
INSERT INTO public.enrollment VALUES (30, 29, '2024-12-16 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (30, 38, '2025-02-19 00:00:00', '9.60');
INSERT INTO public.enrollment VALUES (31, 26, '2024-10-09 00:00:00', '8.20');
INSERT INTO public.enrollment VALUES (31, 30, '2024-11-28 00:00:00', '7.00');
INSERT INTO public.enrollment VALUES (31, 36, '2025-02-08 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (32, 28, '2024-12-12 00:00:00', '9.80');
INSERT INTO public.enrollment VALUES (32, 31, '2025-01-30 00:00:00', '8.25');
INSERT INTO public.enrollment VALUES (32, 40, '2025-02-23 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (33, 22, '2024-10-06 00:00:00', '6.45');
INSERT INTO public.enrollment VALUES (33, 27, '2024-11-24 00:00:00', '7.35');
INSERT INTO public.enrollment VALUES (33, 34, '2025-02-14 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (35, 21, '2024-11-05 00:00:00', '8.45');
INSERT INTO public.enrollment VALUES (35, 23, '2024-12-03 00:00:00', '7.90');
INSERT INTO public.enrollment VALUES (35, 26, '2025-01-12 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (35, 31, '2025-02-19 00:00:00', '9.30');
INSERT INTO public.enrollment VALUES (36, 22, '2024-11-15 00:00:00', '7.50');
INSERT INTO public.enrollment VALUES (36, 27, '2025-01-17 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (36, 33, '2025-02-10 00:00:00', '8.75');
INSERT INTO public.enrollment VALUES (37, 25, '2024-10-18 00:00:00', '6.80');
INSERT INTO public.enrollment VALUES (37, 28, '2024-12-11 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (37, 30, '2025-01-25 00:00:00', '8.20');
INSERT INTO public.enrollment VALUES (37, 35, '2025-02-22 00:00:00', '9.10');
INSERT INTO public.enrollment VALUES (38, 26, '2024-10-30 00:00:00', '7.25');
INSERT INTO public.enrollment VALUES (38, 29, '2024-11-29 00:00:00', '8.00');
INSERT INTO public.enrollment VALUES (38, 32, '2025-01-14 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (38, 36, '2025-02-18 00:00:00', '9.00');
INSERT INTO public.enrollment VALUES (39, 27, '2024-11-10 00:00:00', '6.50');
INSERT INTO public.enrollment VALUES (39, 30, '2024-12-05 00:00:00', '7.85');
INSERT INTO public.enrollment VALUES (39, 33, '2025-01-20 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (39, 37, '2025-02-28 00:00:00', '9.45');
INSERT INTO public.enrollment VALUES (40, 21, '2024-10-22 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (40, 25, '2024-11-12 00:00:00', '7.40');
INSERT INTO public.enrollment VALUES (40, 28, '2025-01-05 00:00:00', '8.90');
INSERT INTO public.enrollment VALUES (40, 34, '2025-02-13 00:00:00', '9.60');
INSERT INTO public.enrollment VALUES (41, 22, '2024-11-01 00:00:00', '8.15');
INSERT INTO public.enrollment VALUES (41, 29, '2024-12-14 00:00:00', '6.75');
INSERT INTO public.enrollment VALUES (41, 32, '2025-01-09 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (41, 38, '2025-02-20 00:00:00', '7.95');
INSERT INTO public.enrollment VALUES (42, 23, '2024-10-12 00:00:00', '7.35');
INSERT INTO public.enrollment VALUES (42, 26, '2024-11-22 00:00:00', '9.00');
INSERT INTO public.enrollment VALUES (42, 31, '2025-01-19 00:00:00', '8.40');
INSERT INTO public.enrollment VALUES (42, 40, '2025-02-23 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (43, 24, '2024-11-03 00:00:00', '8.65');
INSERT INTO public.enrollment VALUES (44, 25, '2024-10-28 00:00:00', '7.50');
INSERT INTO public.enrollment VALUES (44, 28, '2024-11-18 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (44, 33, '2025-01-30 00:00:00', '8.10');
INSERT INTO public.enrollment VALUES (45, 32, '2025-01-12 00:00:00', '9.40');
INSERT INTO public.enrollment VALUES (45, 34, '2025-02-25 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (46, 21, '2024-11-06 00:00:00', '6.80');
INSERT INTO public.enrollment VALUES (46, 24, '2024-12-04 00:00:00', '7.40');
INSERT INTO public.enrollment VALUES (46, 29, '2025-01-10 00:00:00', '8.55');
INSERT INTO public.enrollment VALUES (46, 36, '2025-02-17 00:00:00', '8.95');
INSERT INTO public.enrollment VALUES (47, 23, '2024-11-18 00:00:00', '7.25');
INSERT INTO public.enrollment VALUES (47, 25, '2024-12-09 00:00:00', '8.00');
INSERT INTO public.enrollment VALUES (47, 30, '2025-01-25 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (47, 37, '2025-02-12 00:00:00', '9.10');
INSERT INTO public.enrollment VALUES (48, 22, '2024-10-21 00:00:00', '7.65');
INSERT INTO public.enrollment VALUES (48, 27, '2024-11-27 00:00:00', '6.95');
INSERT INTO public.enrollment VALUES (48, 34, '2025-01-16 00:00:00', '8.30');
INSERT INTO public.enrollment VALUES (48, 38, '2025-02-04 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (49, 21, '2024-10-10 00:00:00', '8.10');
INSERT INTO public.enrollment VALUES (49, 29, '2024-12-01 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (49, 33, '2025-01-22 00:00:00', '9.50');
INSERT INTO public.enrollment VALUES (50, 22, '2024-11-09 00:00:00', '8.00');
INSERT INTO public.enrollment VALUES (50, 30, '2024-12-13 00:00:00', '7.55');
INSERT INTO public.enrollment VALUES (50, 32, '2025-01-03 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (50, 40, '2025-02-11 00:00:00', '9.15');
INSERT INTO public.enrollment VALUES (51, 24, '2024-10-27 00:00:00', '6.95');
INSERT INTO public.enrollment VALUES (51, 29, '2024-11-24 00:00:00', '7.80');
INSERT INTO public.enrollment VALUES (51, 36, '2025-01-08 00:00:00', '8.40');
INSERT INTO public.enrollment VALUES (51, 39, '2025-02-02 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (52, 25, '2024-11-13 00:00:00', '7.10');
INSERT INTO public.enrollment VALUES (52, 31, '2024-12-02 00:00:00', '8.20');
INSERT INTO public.enrollment VALUES (52, 34, '2025-01-14 00:00:00', '9.00');
INSERT INTO public.enrollment VALUES (52, 37, '2025-02-24 00:00:00', '9.45');
INSERT INTO public.enrollment VALUES (53, 21, '2024-11-04 00:00:00', '8.75');
INSERT INTO public.enrollment VALUES (53, 26, '2024-12-06 00:00:00', '7.30');
INSERT INTO public.enrollment VALUES (53, 32, '2025-01-18 00:00:00', '9.60');
INSERT INTO public.enrollment VALUES (53, 39, '2025-02-15 00:00:00', NULL);
INSERT INTO public.enrollment VALUES (54, 23, '2024-10-19 00:00:00', '7.50');
INSERT INTO public.enrollment VALUES (54, 28, '2024-11-10 00:00:00', '8.10');
INSERT INTO public.enrollment VALUES (54, 35, '2025-01-22 00:00:00', '8.30');
INSERT INTO public.enrollment VALUES (54, 38, '2025-02-13 00:00:00', '9.20');
INSERT INTO public.enrollment VALUES (55, 21, '2024-10-12 00:00:00', '8.25');
INSERT INTO public.enrollment VALUES (55, 24, '2024-11-05 00:00:00', '6.90');
INSERT INTO public.enrollment VALUES (55, 32, '2025-01-03 00:00:00', '8.80');
INSERT INTO public.enrollment VALUES (56, 27, '2024-11-14 00:00:00', '7.40');
INSERT INTO public.enrollment VALUES (56, 33, '2025-01-07 00:00:00', '8.25');
INSERT INTO public.enrollment VALUES (56, 39, '2025-02-18 00:00:00', '9.10');
INSERT INTO public.enrollment VALUES (57, 38, '2025-02-03 00:00:00', '9.35');
INSERT INTO public.enrollment VALUES (58, 24, '2024-10-16 00:00:00', '7.80');
INSERT INTO public.enrollment VALUES (58, 29, '2024-11-11 00:00:00', '9.00');
INSERT INTO public.enrollment VALUES (58, 33, '2025-01-15 00:00:00', '8.15');
INSERT INTO public.enrollment VALUES (59, 28, '2024-11-03 00:00:00', '7.20');
INSERT INTO public.enrollment VALUES (59, 30, '2025-01-11 00:00:00', '8.50');
INSERT INTO public.enrollment VALUES (60, 23, '2024-11-04 00:00:00', '7.90');
INSERT INTO public.enrollment VALUES (60, 25, '2024-12-02 00:00:00', '8.00');
INSERT INTO public.enrollment VALUES (60, 32, '2025-01-13 00:00:00', '9.20');
INSERT INTO public.enrollment VALUES (60, 34, '2025-02-14 00:00:00', NULL);


--
-- Data for Name: flashcard; Type: TABLE DATA; Schema: public; Owner: -
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
-- Data for Name: flashcardsession; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.flashcardsession VALUES (5, 10, 21, '2024-03-10 09:30:00', 5);
INSERT INTO public.flashcardsession VALUES (6, 22, 39, '2024-03-12 10:00:00', 8);
INSERT INTO public.flashcardsession VALUES (7, 31, 39, '2024-03-15 11:30:00', 6);
INSERT INTO public.flashcardsession VALUES (8, 13, 28, '2024-03-18 08:45:00', 4);


--
-- Data for Name: grade; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.grade VALUES (1, 10, 8.75, '2024-03-10 10:30:00');
INSERT INTO public.grade VALUES (1, 22, 9, '2024-03-12 11:00:00');
INSERT INTO public.grade VALUES (2, 31, 7.5, '2024-03-15 16:00:00');
INSERT INTO public.grade VALUES (3, 13, 6.8, '2024-03-18 09:30:00');


--
-- Data for Name: material; Type: TABLE DATA; Schema: public; Owner: -
--

-- Algoritmica_Grafurilor (courseid = 21)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (1, 'agr1.pdf', 21, 'cursuri/Algoritmica_Grafurilor/agr1.pdf', 1);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (2, 'agr10.pdf', 21, 'cursuri/Algoritmica_Grafurilor/agr10.pdf', 2);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (3, 'agr11.pdf', 21, 'cursuri/Algoritmica_Grafurilor/agr11.pdf', 3);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (4, 'agr12.pdf', 21, 'cursuri/Algoritmica_Grafurilor/agr12.pdf', 4);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (5, 'agr2.pdf', 21, 'cursuri/Algoritmica_Grafurilor/agr2.pdf', 5);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (6, 'agr3.pdf', 21, 'cursuri/Algoritmica_Grafurilor/agr3.pdf', 6);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (7, 'agr4.pdf', 21, 'cursuri/Algoritmica_Grafurilor/agr4.pdf', 7);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (8, 'agr5.pdf', 21, 'cursuri/Algoritmica_Grafurilor/agr5.pdf', 8);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (9, 'agr6.pdf', 21, 'cursuri/Algoritmica_Grafurilor/agr6.pdf', 9);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (10, 'agr7.pdf', 21, 'cursuri/Algoritmica_Grafurilor/agr7.pdf', 10);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (11, 'agr8.pdf', 21, 'cursuri/Algoritmica_Grafurilor/agr8.pdf', 11);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (12, 'agr9.pdf', 21, 'cursuri/Algoritmica_Grafurilor/agr9.pdf', 12);

-- Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare (courseid = 22)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (13, 'curs1.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/antepartial/curs1.pdf', 13);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (14, 'curs2.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/antepartial/curs2.pdf', 14);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (15, 'curs3.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/antepartial/curs3.pdf', 15);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (16, 'curs4.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/antepartial/curs4.pdf', 16);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (17, 'curs5.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/antepartial/curs5.pdf', 17);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (18, 'curs6.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/antepartial/curs6.pdf', 18);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (19, 'curs7.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/antepartial/curs7.pdf', 19);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (20, 'Curs1-asm-ro.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/postpartial/Curs1-asm-ro.pdf', 20);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (21, 'curs10.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/postpartial/curs10.pdf', 21);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (22, 'curs11.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/postpartial/curs11.pdf', 22);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (23, 'curs12.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/postpartial/curs12.pdf', 23);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (24, 'curs13.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/postpartial/curs13.pdf', 24);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (25, 'Curs2-asm-ro.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/postpartial/Curs2-asm-ro.pdf', 25);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (26, 'curs3-asm-ro.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/postpartial/curs3-asm-ro.pdf', 26);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (27, 'curs4-asm-ro.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/postpartial/curs4-asm-ro.pdf', 27);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (28, 'curs5-asm-ro.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/postpartial/curs5-asm-ro.pdf', 28);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (29, 'curs6-asm-ro.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/postpartial/curs6-asm-ro.pdf', 29);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (30, 'curs8.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/postpartial/curs8.pdf', 30);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (31, 'curs9.pdf', 22, 'cursuri/Arhitectura_Calculatoarelor_Si_Sisteme_De_Operare/postpartial/curs9.pdf', 31);

-- Baze_De_Date (courseid = 23)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (32, 'Curs10-11.ROPhysicaldesign.pdf', 23, 'cursuri/Baze_De_Date/Curs10-11.ROPhysicaldesign.pdf', 32);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (33, 'Curs12-13.RO-Indexes.pdf', 23, 'cursuri/Baze_De_Date/Curs12-13.RO-Indexes.pdf', 33);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (34, 'Curs14.ROQueryProcessing.pdf', 23, 'cursuri/Baze_De_Date/Curs14.ROQueryProcessing.pdf', 34);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (35, 'Curs2-BD.pdf', 23, 'cursuri/Baze_De_Date/Curs2-BD.pdf', 35);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (36, 'Curs3-BD.pdf', 23, 'cursuri/Baze_De_Date/Curs3-BD.pdf', 36);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (37, 'Curs4-BD.pdf', 23, 'cursuri/Baze_De_Date/Curs4-BD.pdf', 37);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (38, 'Curs5-6-BD.pdf', 23, 'cursuri/Baze_De_Date/Curs5-6-BD.pdf', 38);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (39, 'Curs9.ROERModeling.pdf', 23, 'cursuri/Baze_De_Date/Curs9.ROERModeling.pdf', 39);

-- Calcul_Numeric (courseid = 24)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (40, 'CN-curs01-2025.pdf', 24, 'cursuri/Calcul_Numeric/CN-curs01-2025.pdf', 40);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (41, 'CN-curs02-2025.pdf', 24, 'cursuri/Calcul_Numeric/CN-curs02-2025.pdf', 41);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (43, 'CN-curs03-2025.pdf', 24, 'cursuri/Calcul_Numeric/CN-curs03-2025.pdf', 42);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (44, 'CN-curs04-2025.pdf', 24, 'cursuri/Calcul_Numeric/CN-curs04-2025.pdf', 43);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (45, 'CN-curs05-2025.pdf', 24, 'cursuri/Calcul_Numeric/CN-curs05-2025.pdf', 44);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (46, 'CN-curs06-2025.pdf', 24, 'cursuri/Calcul_Numeric/CN-curs06-2025.pdf', 45);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (47, 'CN-curs07-2025.pdf', 24, 'cursuri/Calcul_Numeric/CN-curs07-2025.pdf', 46);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (48, 'CN-curs08-2025.pdf', 24, 'cursuri/Calcul_Numeric/CN-curs08-2025.pdf', 47);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (49, 'CN-curs09-2025.pdf', 24, 'cursuri/Calcul_Numeric/CN-curs09-2025.pdf', 48);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (50, 'CN-curs10+11-2025.pdf', 24, 'cursuri/Calcul_Numeric/CN-curs10+11-2025.pdf', 49);

-- Fundamentele_Algebrice_Ale_Informaticii (courseid = 25)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (51, '12AFCS-RingsI.pdf', 25, 'cursuri/Fundamentele_Algebrice_Ale_Informaticii/12AFCS-RingsI.pdf', 50);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (52, '13AFCS-RingsII.pdf', 25, 'cursuri/Fundamentele_Algebrice_Ale_Informaticii/13AFCS-RingsII.pdf', 51);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (53, '14AES_unlocked.pdf', 25, 'cursuri/Fundamentele_Algebrice_Ale_Informaticii/14AES_unlocked.pdf', 52);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (54, '15+16AFCS-Vector_SpacesI+basis+and+dimensions.pdf', 25, 'cursuri/Fundamentele_Algebrice_Ale_Informaticii/15+16AFCS-Vector_SpacesI+basis+and+dimensions.pdf', 53);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (55, '17+18AFCS-Vector_SpacesII+Linear_maps+inner+product+orthogonality.pdf', 25, 'cursuri/Fundamentele_Algebrice_Ale_Informaticii/17+18AFCS-Vector_SpacesII+Linear_maps+inner+product+orthogonality.pdf', 54);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (56, '19AFCS-Applications_to_Linear_Codes_unlocked.pdf', 25, 'cursuri/Fundamentele_Algebrice_Ale_Informaticii/19AFCS-Applications_to_Linear_Codes_unlocked.pdf', 55);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (57, '1AFCS-Closures.pdf', 25, 'cursuri/Fundamentele_Algebrice_Ale_Informaticii/1AFCS-Closures.pdf', 56);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (58, '2AFCS-NumberTheoryI.pdf', 25, 'cursuri/Fundamentele_Algebrice_Ale_Informaticii/2AFCS-NumberTheoryI.pdf', 57);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (59, '3AFCS-NumberTheoryII.pdf', 25, 'cursuri/Fundamentele_Algebrice_Ale_Informaticii/3AFCS-NumberTheoryII.pdf', 58);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (60, '4AFCS-NumberTheoryIII.pdf', 25, 'cursuri/Fundamentele_Algebrice_Ale_Informaticii/4AFCS-NumberTheoryIII.pdf', 59);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (61, '5AFCS-AppInCrypto.pdf', 25, 'cursuri/Fundamentele_Algebrice_Ale_Informaticii/5AFCS-AppInCrypto.pdf', 60);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (62, '6AFCS-Semigroups_and_Monoids.pdf', 25, 'cursuri/Fundamentele_Algebrice_Ale_Informaticii/6AFCS-Semigroups_and_Monoids.pdf', 61);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (63, '7AFCS-Variable_Length_Codes.pdf', 25, 'cursuri/Fundamentele_Algebrice_Ale_Informaticii/7AFCS-Variable_Length_Codes.pdf', 62);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (64, '8AFCS-Huffman_Codes.pdf', 25, 'cursuri/Fundamentele_Algebrice_Ale_Informaticii/8AFCS-Huffman_Codes.pdf', 63);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (65, '9+10+11AFCS-Groups+discrete_log+applications+in+cryptography.pdf', 25, 'cursuri/Fundamentele_Algebrice_Ale_Informaticii/9+10+11AFCS-Groups+discrete_log+applications+in+cryptography.pdf', 64);

-- Grafică_pe_Calculator (courseid = 26)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (66, '02Rasterisation_pub.pdf', 26, 'cursuri/Grafica_Pe_Calculator_Si_Geometrie_Computationala/02Rasterisation_pub.pdf', 65);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (67, '03Rasterisation2_pub.pdf', 26, 'cursuri/Grafica_Pe_Calculator_Si_Geometrie_Computationala/03Rasterisation2_pub.pdf', 66);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (68, '04Antialiasing_pub.pdf', 26, 'cursuri/Grafica_Pe_Calculator_Si_Geometrie_Computationala/04Antialiasing_pub.pdf', 67);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (69, '05GeometricTransformations_pub.pdf', 26, 'cursuri/Grafica_Pe_Calculator_Si_Geometrie_Computationala/05GeometricTransformations_pub.pdf', 68);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (70, '06Projection_pub.pdf', 26, 'cursuri/Grafica_Pe_Calculator_Si_Geometrie_Computationala/06Projection_pub.pdf', 69);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (71, '07shaders_pub.pdf', 26, 'cursuri/Grafica_Pe_Calculator_Si_Geometrie_Computationala/07shaders_pub.pdf', 70);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (72, '08numericalSimulation_pub.pdf', 26, 'cursuri/Grafica_Pe_Calculator_Si_Geometrie_Computationala/08numericalSimulation_pub.pdf', 71);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (73, '09vertexBufferObjects_pub.pdf', 26, 'cursuri/Grafica_Pe_Calculator_Si_Geometrie_Computationala/09vertexBufferObjects_pub.pdf', 72);

-- Ingineria_Programării (courseid = 27)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (74, 'IP01.pdf', 27, 'cursuri/Ingineria_Programarii/IP01.pdf', 73);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (75, 'IP02.pdf', 27, 'cursuri/Ingineria_Programarii/IP02.pdf', 74);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (76, 'IP03.pdf', 27, 'cursuri/Ingineria_Programarii/IP03.pdf', 75);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (77, 'IP04.pdf', 27, 'cursuri/Ingineria_Programarii/IP04.pdf', 76);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (78, 'IP05.pdf', 27, 'cursuri/Ingineria_Programarii/IP05.pdf', 77);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (79, 'IP06.pdf', 27, 'cursuri/Ingineria_Programarii/IP06.pdf', 78);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (80, 'IP07.pdf', 27, 'cursuri/Ingineria_Programarii/IP07.pdf', 79);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (81, 'IP08.pdf', 27, 'cursuri/Ingineria_Programarii/IP08.pdf', 80);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (82, 'IP09.pdf', 27, 'cursuri/Ingineria_Programarii/IP09.pdf', 81);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (83, 'IP10.pdf', 27, 'cursuri/Ingineria_Programarii/IP10.pdf', 82);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (84, 'IP11.pdf', 27, 'cursuri/Ingineria_Programarii/IP11.pdf', 83);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (85, 'IP12.pdf', 27, 'cursuri/Ingineria_Programarii/IP12.pdf', 84);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (86, 'IP13.pdf', 27, 'cursuri/Ingineria_Programarii/IP13.pdf', 85);

-- Inteligența_Artificială (courseid = 28)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (87, 'IA_10_PLN.pdf', 28, 'cursuri/Inteligenta_Artificiala/IA_10_PLN.pdf', 86);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (88, 'IA_11_curs_retele_bayes.pdf', 28, 'cursuri/Inteligenta_Artificiala/IA_11_curs_retele_bayes.pdf', 87);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (89, 'IA_12_curs_planificare.pdf', 28, 'cursuri/Inteligenta_Artificiala/IA_12_curs_planificare.pdf', 88);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (90, 'IA_2_SBM_I.pdf', 28, 'cursuri/Inteligenta_Artificiala/IA_2_SBM_I.pdf', 89);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (91, 'IA_3_SBM_II.pdf', 28, 'cursuri/Inteligenta_Artificiala/IA_3_SBM_II.pdf', 90);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (92, 'IA_4_curs_CSP.pdf', 28, 'cursuri/Inteligenta_Artificiala/IA_4_curs_CSP.pdf', 91);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (93, 'IA_5_GT.pdf', 28, 'cursuri/Inteligenta_Artificiala/IA_5_GT.pdf', 92);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (94, 'IA_6_curs_NN.pdf', 28, 'cursuri/Inteligenta_Artificiala/IA_6_curs_NN.pdf', 93);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (95, 'IA_7_curs_MDP.pdf', 28, 'cursuri/Inteligenta_Artificiala/IA_7_curs_MDP.pdf', 94);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (96, 'IA_8_curs_RL.pdf', 28, 'cursuri/Inteligenta_Artificiala/IA_8_curs_RL.pdf', 95);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (97, 'IA_9_RCO.pdf', 28, 'cursuri/Inteligenta_Artificiala/IA_9_RCO.pdf', 96);

-- Învățare_Automată (courseid = 29)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (98, 'cluster.pdf', 29, 'cursuri/Invatare_Automata/cluster.pdf', 97);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (99, 'foundations.pdf', 29, 'cursuri/Invatare_Automata/foundations.pdf', 98);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (100, 'hmm.pdf', 29, 'cursuri/Invatare_Automata/hmm.pdf', 99);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (101, 'ML.ex-book.SLIDES.ANN.pdf', 29, 'cursuri/Invatare_Automata/ML.ex-book.SLIDES.ANN.pdf', 100);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (102, 'ML.ex-book.SLIDES.Bayes.pdf', 29, 'cursuri/Invatare_Automata/ML.ex-book.SLIDES.Bayes.pdf', 101);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (103, 'ML.ex-book.SLIDES.Cluster.pdf', 29, 'cursuri/Invatare_Automata/ML.ex-book.SLIDES.Cluster.pdf', 102);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (104, 'ML.ex-book.SLIDES.DT.pdf', 29, 'cursuri/Invatare_Automata/ML.ex-book.SLIDES.DT.pdf', 103);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (105, 'ML.ex-book.SLIDES.EM.pdf', 29, 'cursuri/Invatare_Automata/ML.ex-book.SLIDES.EM.pdf', 104);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (106, 'ML.ex-book.SLIDES.IBL.pdf', 29, 'cursuri/Invatare_Automata/ML.ex-book.SLIDES.IBL.pdf', 105);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (107, 'ML.ex-book.SLIDES.ProbStat.pdf', 29, 'cursuri/Invatare_Automata/ML.ex-book.SLIDES.ProbStat.pdf', 106);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (108, 'ML.ex-book.SLIDES.Regression.pdf', 29, 'cursuri/Invatare_Automata/ML.ex-book.SLIDES.Regression.pdf', 107);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (109, 'ML.ex-book.SLIDES.SVM.pdf', 29, 'cursuri/Invatare_Automata/ML.ex-book.SLIDES.SVM.pdf', 108);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (110, 'ml0.pdf', 29, 'cursuri/Invatare_Automata/ml0.pdf', 109);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (111, 'ml13.pdf', 29, 'cursuri/Invatare_Automata/ml13.pdf', 110);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (112, 'ml3.pdf', 29, 'cursuri/Invatare_Automata/ml3.pdf', 111);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (113, 'ml4.pdf', 29, 'cursuri/Invatare_Automata/ml4.pdf', 112);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (114, 'ml5.pdf', 29, 'cursuri/Invatare_Automata/ml5.pdf', 113);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (115, 'ml6.pdf', 29, 'cursuri/Invatare_Automata/ml6.pdf', 114);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (116, 'ml7.pdf', 29, 'cursuri/Invatare_Automata/ml7.pdf', 115);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (117, 'ml8.pdf', 29, 'cursuri/Invatare_Automata/ml8.pdf', 116);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (118, 'svm.pdf', 29, 'cursuri/Invatare_Automata/svm.pdf', 117);

-- Limbaje_Formale_Automate_Si_Compilatoare (courseid = 30)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (119, 'LFAC07.pdf', 30, 'cursuri/Limbaje_Formale_Automate_Si_Compilatoare/LFAC07.pdf', 118);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (120, 'LFAC08.pdf', 30, 'cursuri/Limbaje_Formale_Automate_Si_Compilatoare/LFAC08.pdf', 119);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (121, 'LFAC09.pdf', 30, 'cursuri/Limbaje_Formale_Automate_Si_Compilatoare/LFAC09.pdf', 120);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (123, 'lfac1.pdf', 30, 'cursuri/Limbaje_Formale_Automate_Si_Compilatoare/lfac1.pdf', 121);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (124, 'LFAC10.pdf', 30, 'cursuri/Limbaje_Formale_Automate_Si_Compilatoare/LFAC10.pdf', 122);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (125, 'LFAC11.pdf', 30, 'cursuri/Limbaje_Formale_Automate_Si_Compilatoare/LFAC11.pdf', 123);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (126, 'LFAC12.pdf', 30, 'cursuri/Limbaje_Formale_Automate_Si_Compilatoare/LFAC12.pdf', 124);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (127, 'lfac2.pdf', 30, 'cursuri/Limbaje_Formale_Automate_Si_Compilatoare/lfac2.pdf', 125);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (128, 'lfac3.pdf', 30, 'cursuri/Limbaje_Formale_Automate_Si_Compilatoare/lfac3.pdf', 126);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (129, 'lfac4.pdf', 30, 'cursuri/Limbaje_Formale_Automate_Si_Compilatoare/lfac4.pdf', 127);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (130, 'lfac5.pdf', 30, 'cursuri/Limbaje_Formale_Automate_Si_Compilatoare/lfac5.pdf', 128);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (131, 'lfac6.pdf', 30, 'cursuri/Limbaje_Formale_Automate_Si_Compilatoare/lfac6.pdf', 129);

-- Logica_Pentru_Informatica (courseid = 31)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (132, 'antepartial.pdf', 31, 'cursuri/Logica_Pentru_Informatica/antepartial.pdf', 130);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (133, 'postpartial.pdf', 31, 'cursuri/Logica_Pentru_Informatica/postpartial.pdf', 131);

-- Matematica_Calcul_Diferential_Si_Integral (courseid 41)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (134, 'Curs01.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/Curs01.pdf', 134);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (135, 'Curs02.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/Curs02.pdf', 135);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (136, 'Curs03.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/Curs03.pdf', 136);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (137, 'Curs04.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/Curs04.pdf', 137);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (138, 'Curs05.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/Curs05.pdf', 138);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (139, 'Curs06.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/Curs06.pdf', 139);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (140, 'Curs07.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/Curs07.pdf', 140);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (141, 'Curs08.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/Curs08.pdf', 141);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (142, 'Curs09.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/Curs09.pdf', 142);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (143, 'Curs10.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/Curs10.pdf', 143);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (144, 'Curs11.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/Curs11.pdf', 144);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (145, 'NC01.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/NC01.pdf', 145);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (146, 'NC02.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/NC02.pdf', 146);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (147, 'NC03.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/NC03.pdf', 147);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (148, 'NC04.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/NC04.pdf', 148);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (149, 'NC05.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/NC05.pdf', 149);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (150, 'NC06.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/NC06.pdf', 150);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (151, 'NC07.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/NC07.pdf', 151);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (152, 'NC08.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/NC08.pdf', 152);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (153, 'NC09.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/NC09.pdf', 153);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (154, 'NC10.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/NC10.pdf', 154);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (155, 'NC11.pdf', 41, 'cursuri/Matematica_Calcul_Diferential_Si_Integral/NC11.pdf', 155);

-- Practica_Sisteme_De_Gestiune_Pentru_Baze_De_Date (courseid 42)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (156, 'SGBD123.pdf', 42, 'cursuri/Practica_Sisteme_De_Gestiune_Pentru_Baze_De_Date/SGBD123.pdf', 156);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (157, 'SGBD456.pdf', 42, 'cursuri/Practica_Sisteme_De_Gestiune_Pentru_Baze_De_Date/SGBD456.pdf', 157);

-- Probabilitati_Si_Statistica (courseid 32)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (158, 'probability1.pdf', 32, 'cursuri/Probabilitati_Si_Statistica/probability1.pdf', 158);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (159, 'probability10.pdf', 32, 'cursuri/Probabilitati_Si_Statistica/probability10.pdf', 159);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (160, 'probability11.pdf', 32, 'cursuri/Probabilitati_Si_Statistica/probability11.pdf', 160);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (161, 'probability12.pdf', 32, 'cursuri/Probabilitati_Si_Statistica/probability12.pdf', 161);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (162, 'probability2.pdf', 32, 'cursuri/Probabilitati_Si_Statistica/probability2.pdf', 162);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (163, 'probability3.pdf', 32, 'cursuri/Probabilitati_Si_Statistica/probability3.pdf', 163);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (164, 'probability4.pdf', 32, 'cursuri/Probabilitati_Si_Statistica/probability4.pdf', 164);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (165, 'probability5.pdf', 32, 'cursuri/Probabilitati_Si_Statistica/probability5.pdf', 165);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (166, 'probability6.pdf', 32, 'cursuri/Probabilitati_Si_Statistica/probability6.pdf', 166);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (167, 'probability7.pdf', 32, 'cursuri/Probabilitati_Si_Statistica/probability7.pdf', 167);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (168, 'probability8.pdf', 32, 'cursuri/Probabilitati_Si_Statistica/probability8.pdf', 168);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (169, 'probability9.pdf', 32, 'cursuri/Probabilitati_Si_Statistica/probability9.pdf', 169);

-- Programare_Avansata (courseid 33)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (170, 'collections_slide_en.pdf', 33, 'cursuri/Programare_Avansata/collections_slide_en.pdf', 170);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (171, 'concurrency_slide_en.pdf', 33, 'cursuri/Programare_Avansata/concurrency_slide_en.pdf', 171);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (172, 'exceptions_slide_en.pdf', 33, 'cursuri/Programare_Avansata/exceptions_slide_en.pdf', 172);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (173, 'generics_slide_en.pdf', 33, 'cursuri/Programare_Avansata/generics_slide_en.pdf', 173);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (174, 'gui_slide_en.pdf', 33, 'cursuri/Programare_Avansata/gui_slide_en.pdf', 174);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (175, 'i18n_slide_en.pdf', 33, 'cursuri/Programare_Avansata/i18n_slide_en.pdf', 175);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (176, 'input_output_slide_en.pdf', 33, 'cursuri/Programare_Avansata/input_output_slide_en.pdf', 176);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (177, 'interfaces_slide_en.pdf', 33, 'cursuri/Programare_Avansata/interfaces_slide_en.pdf', 177);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (178, 'jdbc_slide_en.pdf', 33, 'cursuri/Programare_Avansata/jdbc_slide_en.pdf', 178);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (179, 'jpa_intro_slide_en.pdf', 33, 'cursuri/Programare_Avansata/jpa_intro_slide_en.pdf', 179);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (180, 'networking_slide_en.pdf', 33, 'cursuri/Programare_Avansata/networking_slide_en.pdf', 180);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (181, 'objects_classes_slide_en.pdf', 33, 'cursuri/Programare_Avansata/objects_classes_slide_en.pdf', 181);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (182, 'reflection_slide_en.pdf', 33, 'cursuri/Programare_Avansata/reflection_slide_en.pdf', 182);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (183, 'spring_slide_en.pdf', 33, 'cursuri/Programare_Avansata/spring_slide_en.pdf', 183);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (184, 'streams_slide_en.pdf', 33, 'cursuri/Programare_Avansata/streams_slide_en.pdf', 184);

-- Programare_Orientata_Obiect (courseid 34)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (185, 'Curs-1.pdf', 34, 'cursuri/Programare_Orientata_Obiect/Curs-1.pdf', 185);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (186, 'Curs-2.pdf', 34, 'cursuri/Programare_Orientata_Obiect/Curs-2.pdf', 186);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (187, 'curs-3.pdf', 34, 'cursuri/Programare_Orientata_Obiect/curs-3.pdf', 187);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (188, 'curs-4.pdf', 34, 'cursuri/Programare_Orientata_Obiect/curs-4.pdf', 188);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (189, 'curs-5.pdf', 34, 'cursuri/Programare_Orientata_Obiect/curs-5.pdf', 189);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (190, 'curs-6.pdf', 34, 'cursuri/Programare_Orientata_Obiect/curs-6.pdf', 190);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (191, 'curs-7.pdf', 34, 'cursuri/Programare_Orientata_Obiect/curs-7.pdf', 191);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (192, 'curs-8.pdf', 34, 'cursuri/Programare_Orientata_Obiect/curs-8.pdf', 192);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (193, 'curs-9.pdf', 34, 'cursuri/Programare_Orientata_Obiect/curs-9.pdf', 193);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (194, 'design-patterns.pdf', 34, 'cursuri/Programare_Orientata_Obiect/design-patterns.pdf', 194);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (195, 'modeling.pdf', 34, 'cursuri/Programare_Orientata_Obiect/modeling.pdf', 195);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (196, 'testing.pdf', 34, 'cursuri/Programare_Orientata_Obiect/testing.pdf', 196);

-- Programare_Python (courseid 43)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (197, 'Curs-1.pdf', 43, 'cursuri/Programare_Python/Curs-1.pdf', 197);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (198, 'Curs-10.pdf', 43, 'cursuri/Programare_Python/Curs-10.pdf', 198);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (199, 'Curs-11.pdf', 43, 'cursuri/Programare_Python/Curs-11.pdf', 199);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (200, 'Curs-12.pdf', 43, 'cursuri/Programare_Python/Curs-12.pdf', 200);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (201, 'Curs-2.pdf', 43, 'cursuri/Programare_Python/Curs-2.pdf', 201);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (202, 'Curs-3.pdf', 43, 'cursuri/Programare_Python/Curs-3.pdf', 202);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (203, 'Curs-4.pdf', 43, 'cursuri/Programare_Python/Curs-4.pdf', 203);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (204, 'Curs-5.pdf', 43, 'cursuri/Programare_Python/Curs-5.pdf', 204);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (205, 'Curs-6.pdf', 43, 'cursuri/Programare_Python/Curs-6.pdf', 205);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (206, 'Curs-7.pdf', 43, 'cursuri/Programare_Python/Curs-7.pdf', 206);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (207, 'Curs-8.pdf', 43, 'cursuri/Programare_Python/Curs-8.pdf', 207);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (208, 'Curs-9.pdf', 43, 'cursuri/Programare_Python/Curs-9.pdf', 208);

-- Programare_Rust (courseid 44 )
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (209, 'Course-1.pdf', 44, 'cursuri/Programare_Rust/Course-1.pdf', 209);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (210, 'Course-10.pdf', 44, 'cursuri/Programare_Rust/Course-10.pdf', 210);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (211, 'Course-11.pdf', 44, 'cursuri/Programare_Rust/Course-11.pdf', 211);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (212, 'Course-12.pdf', 44, 'cursuri/Programare_Rust/Course-12.pdf', 212);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (213, 'Course-2.pdf', 44, 'cursuri/Programare_Rust/Course-2.pdf', 213);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (214, 'Course-3.pdf', 44, 'cursuri/Programare_Rust/Course-3.pdf', 214);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (215, 'Course-4.pdf', 44, 'cursuri/Programare_Rust/Course-4.pdf', 215);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (216, 'Course-5.pdf', 44, 'cursuri/Programare_Rust/Course-5.pdf', 216);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (217, 'Course-6.pdf', 44, 'cursuri/Programare_Rust/Course-6.pdf', 217);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (218, 'Course-7.pdf', 44, 'cursuri/Programare_Rust/Course-7.pdf', 218);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (219, 'Course-8.pdf', 44, 'cursuri/Programare_Rust/Course-8.pdf', 219);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (220, 'Course-9.pdf', 44, 'cursuri/Programare_Rust/Course-9.pdf', 220);

-- Proiectarea_Algoritmilor (courseid 35)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (221, 'curs+10-greedy.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs10/curs+10-greedy.pdf', 221);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (222, 'greedy-notes.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs10/greedy-notes.pdf', 222);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (223, '9_dyn-prog-notes.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs11+12/9_dyn-prog-notes.pdf', 223);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (224, 'curs+11-dp1.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs11+12/curs+11-dp1.pdf', 224);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (225, 'curs+12-dp2.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs11+12/curs+12-dp2.pdf', 225);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (226, '13+bkt+bb.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs13/13+bkt+bb.pdf', 226);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (227, '13-bkt+bb.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs13/13-bkt+bb.pdf', 227);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (228, '1compl-alg-probl.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs2/1compl-alg-probl.pdf', 228);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (229, '2sorting-des-und.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs2/2sorting-des-und.pdf', 229);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (230, '3computing-wcet.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs2/3computing-wcet.pdf', 230);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (231, '1alg-nedet-prob-en.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs3/1alg-nedet-prob-en.pdf', 231);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (232, '2examples-nondet-alg.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs3/2examples-nondet-alg.pdf', 232);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (233, '3jacobi-symbol.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs3/3jacobi-symbol.pdf', 233);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (234, 'compl-medie-en.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs4/compl-medie-en.pdf', 234);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (235, 'curs+nou.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs5+6/curs+nou.pdf', 235);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (236, 'bm.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs8+9/bm.pdf', 236);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (237, 'curs+8+string-match1-en.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs8+9/curs+8+string-match1-en.pdf', 237);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (238, 'kmp.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs8+9/kmp.pdf', 238);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (239, 'rk.pdf', 35, 'cursuri/Proiectarea_Algoritmilor/curs8+9/rk.pdf', 239);

-- Retele_De_Calculatoare (courseid 36)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (240, '10rc_NivelulAplicatie_Ro.pdf', 36, 'cursuri/Retele_De_Calculatoare/10rc_NivelulAplicatie_Ro.pdf', 240);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (241, '11rc_ParadigmaP2P_Ro.pdf', 36, 'cursuri/Retele_De_Calculatoare/11rc_ParadigmaP2P_Ro.pdf', 241);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (242, '11rc_ParadigmaRPC_Ro.pdf', 36, 'cursuri/Retele_De_Calculatoare/11rc_ParadigmaRPC_Ro.pdf', 242);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (243, '13rc_ReteleWireless_RO.pdf', 36, 'cursuri/Retele_De_Calculatoare/13rc_ReteleWireless_RO.pdf', 243);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (244, '14rc_SecuritateRC_RO.pdf', 36, 'cursuri/Retele_De_Calculatoare/14rc_SecuritateRC_RO.pdf', 244);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (245, '2rc_ArhitecturiDeRetea_Ro.pdf', 36, 'cursuri/Retele_De_Calculatoare/2rc_ArhitecturiDeRetea_Ro.pdf', 245);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (246, '3rc_NivelulRetea_Ro.pdf', 36, 'cursuri/Retele_De_Calculatoare/3rc_NivelulRetea_Ro.pdf', 246);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (247, '4rc_NivelulTransport_Ro.pdf', 36, 'cursuri/Retele_De_Calculatoare/4rc_NivelulTransport_Ro.pdf', 247);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (248, '5rc_ProgramareaInReteaI_ro.pdf', 36, 'cursuri/Retele_De_Calculatoare/5rc_ProgramareaInReteaI_ro.pdf', 248);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (249, '6rc_ProgramareaInReteaII_Ro.pdf', 36, 'cursuri/Retele_De_Calculatoare/6rc_ProgramareaInReteaII_Ro.pdf', 249);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (250, '7rc_ProgramareaInReteaIII_Ro.pdf', 36, 'cursuri/Retele_De_Calculatoare/7rc_ProgramareaInReteaIII_Ro.pdf', 250);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (251, '9rc_SistemulNumelorDeDomenii_Ro.pdf', 36, 'cursuri/Retele_De_Calculatoare/9rc_SistemulNumelorDeDomenii_Ro.pdf', 251);

-- Materials for Securitatea_Informației (courseid 37)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (252, '2IS-CryptoBasics_SKC_unlocked.pdf', 37, 'cursuri/Securitatea_Informatiei/2IS-CryptoBasics_SKC_unlocked.pdf', 252);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (253, '3IS-CryptoBasics_PKC_unlocked.pdf', 37, 'cursuri/Securitatea_Informatiei/3IS-CryptoBasics_PKC_unlocked.pdf', 253);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (254, 'curs1-IS-Introduction_to_IS.pdf', 37, 'cursuri/Securitatea_Informatiei/curs1-IS-Introduction_to_IS.pdf', 254);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (255, 'curs11+IS-IPsecurity_unlocked.pdf', 37, 'cursuri/Securitatea_Informatiei/curs11+IS-IPsecurity_unlocked.pdf', 255);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (256, 'curs13-14+IS-EmailSecurity_unlocked.pdf', 37, 'cursuri/Securitatea_Informatiei/curs13-14+IS-EmailSecurity_unlocked.pdf', 256);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (257, 'curs5-IS-Access_Control_DAC_unlocked.pdf', 37, 'cursuri/Securitatea_Informatiei/curs5-IS-Access_Control_DAC_unlocked.pdf', 257);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (258, 'curs5-IS-Access_Control_unlocked.pdf', 37, 'cursuri/Securitatea_Informatiei/curs5-IS-Access_Control_unlocked.pdf', 258);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (259, 'curs6-IS-Access_Control_MAC_unlocked.pdf', 37, 'cursuri/Securitatea_Informatiei/curs6-IS-Access_Control_MAC_unlocked.pdf', 259);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (260, 'curs7-IS-Access_Control_RBAC_unlocked.pdf', 37, 'cursuri/Securitatea_Informatiei/curs7-IS-Access_Control_RBAC_unlocked.pdf', 260);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (261, 'curs9-IS-Access_Control_ABAC_unlocked.pdf', 37, 'cursuri/Securitatea_Informatiei/curs9-IS-Access_Control_ABAC_unlocked.pdf', 261);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (262, 'IS-DNSsec_unlocked.pdf', 37, 'cursuri/Securitatea_Informatiei/IS-DNSsec_unlocked.pdf', 262);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (263, 'IS-TLS_unlocked.pdf', 37, 'cursuri/Securitatea_Informatiei/IS-TLS_unlocked.pdf', 263);

-- Materials for Sisteme_de_Operare (courseid 38)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (264, 'ch1.pdf', 38, 'cursuri/Sisteme_De_Operare/curs1/ch1.pdf', 264);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (265, 'P1.1_intro_web-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs1/P1.1_intro_web-ro.pdf', 265);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (266, 'P1.2_commands1_web-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs1/P1.2_commands1_web-ro.pdf', 266);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (267, 'ch10.pdf', 38, 'cursuri/Sisteme_De_Operare/curs10/ch10.pdf', 267);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (268, 'P10_fork+wait.pdf', 38, 'cursuri/Sisteme_De_Operare/curs10/P10_fork+wait.pdf', 268);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (269, 'T9_memoryAdmin2-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs10/T9_memoryAdmin2-ro.pdf', 269);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (270, 'ch11.pdf', 38, 'cursuri/Sisteme_De_Operare/curs11/ch11.pdf', 270);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (271, 'P11_exec_web-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs11/P11_exec_web-ro.pdf', 271);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (272, 'T10_memoryAdmin3-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs11/T10_memoryAdmin3-ro.pdf', 272);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (273, 'ch13.pdf', 38, 'cursuri/Sisteme_De_Operare/curs12/ch13.pdf', 273);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (274, 'P12_pipe+fifo_web-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs12/P12_pipe+fifo_web-ro.pdf', 274);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (275, 'T11_hddAdmin-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs12/T11_hddAdmin-ro.pdf', 275);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (276, 'ch14.pdf', 38, 'cursuri/Sisteme_De_Operare/curs13/ch14.pdf', 276);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (277, 'P13_signal_web.pdf', 38, 'cursuri/Sisteme_De_Operare/curs13/P13_signal_web.pdf', 277);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (278, 'T12_filesysAdmin-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs13/T12_filesysAdmin-ro.pdf', 278);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (279, 'ch18.pdf', 38, 'cursuri/Sisteme_De_Operare/curs14/ch18.pdf', 279);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (280, 'ch19.pdf', 38, 'cursuri/Sisteme_De_Operare/curs14/ch19.pdf', 280);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (281, 'P14_ncurses.pdf', 38, 'cursuri/Sisteme_De_Operare/curs14/P14_ncurses.pdf', 281);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (282, 'T13_introSistemeDistribuite.pdf', 38, 'cursuri/Sisteme_De_Operare/curs14/T13_introSistemeDistribuite.pdf', 282);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (283, 'ch2.pdf', 38, 'cursuri/Sisteme_De_Operare/curs2/ch2.pdf', 283);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (284, 'T2_structure-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs2/T2_structure-ro.pdf', 284);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (285, 'ch3.pdf', 38, 'cursuri/Sisteme_De_Operare/curs3/ch3.pdf', 285);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (286, 'P3_shell1_web-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs3/P3_shell1_web-ro.pdf', 286);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (287, 'T3_processAdmin1-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs3/T3_processAdmin1-ro.pdf', 287);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (288, 'ch5.pdf', 38, 'cursuri/Sisteme_De_Operare/curs4/ch5.pdf', 288);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (289, 'P4_shell2_web-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs4/P4_shell2_web-ro.pdf', 289);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (290, 'T4_processAdmin2-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs4/T4_processAdmin2-ro.pdf', 290);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (291, 'ch6.pdf', 38, 'cursuri/Sisteme_De_Operare/curs5/ch6.pdf', 291);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (292, 'P5.2_WSL_ppt-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs5/P5.2_WSL_ppt-ro.pdf', 292);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (293, 'T5_processSync1-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs5/T5_processSync1-ro.pdf', 293);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (294, 'ch7.pdf', 38, 'cursuri/Sisteme_De_Operare/curs6/ch7.pdf', 294);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (295, 'P6_files_web-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs6/P6_files_web-ro.pdf', 295);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (296, 'T6_processSync2-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs6/T6_processSync2-ro.pdf', 296);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (297, 'ch8.pdf', 38, 'cursuri/Sisteme_De_Operare/curs7/ch8.pdf', 297);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (298, 'P7_flock_web-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs7/P7_flock_web-ro.pdf', 298);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (299, 'T7_IPC+deadlock-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs7/T7_IPC+deadlock-ro.pdf', 299);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (300, 'ch9.pdf', 38, 'cursuri/Sisteme_De_Operare/curs9/ch9.pdf', 300);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (301, 'P9_mmap_web-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs9/P9_mmap_web-ro.pdf', 301);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (302, 'T8_memoryAdmin1-ro.pdf', 38, 'cursuri/Sisteme_De_Operare/curs9/T8_memoryAdmin1-ro.pdf', 302);

-- Materials for Structuri_de_Date (courseid 39)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (303, 'curs-01.pdf', 39, 'cursuri/Structuri_De_Date/curs-01.pdf', 303);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (304, 'curs-02.pdf', 39, 'cursuri/Structuri_De_Date/curs-02.pdf', 304);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (305, 'curs-03.pdf', 39, 'cursuri/Structuri_De_Date/curs-03.pdf', 305);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (306, 'curs-04.pdf', 39, 'cursuri/Structuri_De_Date/curs-04.pdf', 306);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (307, 'curs-05.pdf', 39, 'cursuri/Structuri_De_Date/curs-05.pdf', 307);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (308, 'curs-06.pdf', 39, 'cursuri/Structuri_De_Date/curs-06.pdf', 308);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (309, 'curs-07.pdf', 39, 'cursuri/Structuri_De_Date/curs-07.pdf', 309);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (310, 'curs-08.pdf', 39, 'cursuri/Structuri_De_Date/curs-08.pdf', 310);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (311, 'curs-09.pdf', 39, 'cursuri/Structuri_De_Date/curs-09.pdf', 311);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (312, 'curs-10.pdf', 39, 'cursuri/Structuri_De_Date/curs-10.pdf', 312);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (313, 'curs-11.pdf', 39, 'cursuri/Structuri_De_Date/curs-11.pdf', 313);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (314, 'curs-12.pdf', 39, 'cursuri/Structuri_De_Date/curs-12.pdf', 314);

-- Materials for Tehnologii_WEB (courseid 40)
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (315, 'web-arhitecturaserverweb.pdf', 40, 'cursuri/Tehnologii_WEB/web-arhitecturaserverweb.pdf', 315);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (316, 'web01arhitecturaweb.pdf', 40, 'cursuri/Tehnologii_WEB/web01arhitecturaweb.pdf', 316);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (317, 'web02programareweb-http-cgi.pdf', 40, 'cursuri/Tehnologii_WEB/web02programareweb-http-cgi.pdf', 317);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (318, 'web03programareweb-http-cookie-sesiune.pdf', 40, 'cursuri/Tehnologii_WEB/web03programareweb-http-cookie-sesiune.pdf', 318);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (319, 'web04dezvoltareaaplicatiilorweb-inginerieweb.pdf', 40, 'cursuri/Tehnologii_WEB/web04dezvoltareaaplicatiilorweb-inginerieweb.pdf', 319);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (320, 'web05dezvoltareaaplicatiilorweb-php.pdf', 40, 'cursuri/Tehnologii_WEB/web05dezvoltareaaplicatiilorweb-php.pdf', 320);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (321, 'web06modelarexml-familiaxml.pdf', 40, 'cursuri/Tehnologii_WEB/web06modelarexml-familiaxml.pdf', 321);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (322, 'web08procesarixml-dom.pdf', 40, 'cursuri/Tehnologii_WEB/web08procesarixml-dom.pdf', 322);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (323, 'web09serviciiweb-rest.pdf', 40, 'cursuri/Tehnologii_WEB/web09serviciiweb-rest.pdf', 323);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (324, 'web10serviciiweb-api-microservicii-serverless.pdf', 40, 'cursuri/Tehnologii_WEB/web10serviciiweb-api-microservicii-serverless.pdf', 324);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (325, 'web11serviciiweb-mashups-spa-pwa.pdf', 40, 'cursuri/Tehnologii_WEB/web11serviciiweb-mashups-spa-pwa.pdf', 325);
INSERT INTO public.material (index, name, courseid, path, materialid) VALUES (326, 'web12securitateweb.pdf', 40, 'cursuri/Tehnologii_WEB/web12securitateweb.pdf', 326);

--
-- Data for Name: streak; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.streak VALUES (1, 10, 3, '2024-03-10');
INSERT INTO public.streak VALUES (2, 11, 5, '2024-03-12');
INSERT INTO public.streak VALUES (3, 12, 2, '2024-03-15');
INSERT INTO public.streak VALUES (4, 13, 4, '2024-03-18');


--
-- Data for Name: test; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.test VALUES (1, 'Test 1', 'Test pentru Algoritmica Grafurilor', '2025-04-10 00:00:00', 66, 21);
INSERT INTO public.test VALUES (2, 'Test 2', 'Test pentru Baze de Date', '2025-04-15 00:00:00', 63, 23);
INSERT INTO public.test VALUES (3, 'Test 3', 'Test pentru Programare Avansată', '2025-04-20 00:00:00', 65, 33);


--
-- Data for Name: testanswer; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.testanswer VALUES (15, 4, false, 'O(n)');
INSERT INTO public.testanswer VALUES (16, 4, true, 'O(n log n)');
INSERT INTO public.testanswer VALUES (17, 4, false, 'O(1)');
INSERT INTO public.testanswer VALUES (18, 4, false, 'O(n^2)');
INSERT INTO public.testanswer VALUES (19, 5, false, 'Un graf neorienta conex');
INSERT INTO public.testanswer VALUES (20, 5, true, 'Un arbore de acoperire minimă conectează toate nodurile unui graf');
INSERT INTO public.testanswer VALUES (21, 6, false, 'O bază de date relațională este mai flexibilă decât una NoSQL');
INSERT INTO public.testanswer VALUES (22, 6, true, 'O bază de date NoSQL este mai scalabilă decât una relațională');
INSERT INTO public.testanswer VALUES (23, 7, true, 'Normalizarea bazei de date se referă la reducerea redundanței');
INSERT INTO public.testanswer VALUES (24, 7, false, 'Normalizarea bazei de date înseamnă adăugarea de date suplimentare pentru a îmbunătăți performanța');
INSERT INTO public.testanswer VALUES (25, 8, true, 'Tehnicile de programare orientată pe obiecte sunt folosite pentru a organiza datele');
INSERT INTO public.testanswer VALUES (26, 8, false, 'Programarea orientată pe obiecte este folosită doar pentru aplicații grafice');
INSERT INTO public.testanswer VALUES (27, 9, false, 'Polimorfismul static presupune decizia legării funcțiilor în timpul execuției');
INSERT INTO public.testanswer VALUES (28, 9, true, 'Polimorfismul static se referă la legarea funcțiilor în timpul compilării');


--
-- Data for Name: testquestion; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.testquestion VALUES (4, 1, 'Care este complexitatea algoritmului Dijkstra?', 2, 4);
INSERT INTO public.testquestion VALUES (5, 1, 'Ce este un arbore de acoperire minimă?', 3, 5);
INSERT INTO public.testquestion VALUES (6, 2, 'Care este diferența dintre o bază de date relațională și una NoSQL?', 2.5, 6);
INSERT INTO public.testquestion VALUES (7, 2, 'Ce înseamnă normalizarea unei baze de date?', 2.5, 7);
INSERT INTO public.testquestion VALUES (8, 3, 'Care este scopul tehnicilor de programare orientată pe obiecte?', 3, 8);
INSERT INTO public.testquestion VALUES (9, 3, 'Explică diferența dintre polimorfismul static și dinamic.', 2.5, 9);


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.users VALUES (1, 'John', 'Doe', 'john.doe@example.com', 'student', 'hashed_password_1');
INSERT INTO public.users VALUES (2, 'Jane', 'Smith', 'jane.smith@example.com', 'profesor', 'hashed_password_2');
INSERT INTO public.users VALUES (3, 'Alice', 'Brown', 'alice.brown@example.com', 'admin', 'hashed_password_3');
INSERT INTO public.users VALUES (4, 'Bob', 'Johnson', 'bob.johnson@example.com', 'student', 'hashed_password_4');
INSERT INTO public.users VALUES (5, 'Emily', 'Davis', 'emily.davis@example.com', 'profesor', 'hashed_password_5');
INSERT INTO public.users VALUES (6, 'Andrei', 'Popescu', 'andrei@student.com', 'student', 'parola123');
INSERT INTO public.users VALUES (7, 'Ioana', 'Marinescu', 'ioana@profesor.com', 'profesor', 'parola456');
INSERT INTO public.users VALUES (8, 'Mihai', 'Radu', 'mihai@admin.com', 'admin', 'adminpass');
INSERT INTO public.users VALUES (10, 'Maria', 'Ionescu', 'maria.ionescu@student.com', 'student', 'z7Lp3Xq!');
INSERT INTO public.users VALUES (11, 'Vlad', 'Georgescu', 'vlad.georgescu@student.com', 'student', 'Qw92rTY@');
INSERT INTO public.users VALUES (12, 'Ana', 'Dumitru', 'ana.dumitru@student.com', 'student', 'mN4#vT8e');
INSERT INTO public.users VALUES (13, 'Radu', 'Enache', 'radu.enache@student.com', 'student', 'Yt3!pLo9');
INSERT INTO public.users VALUES (14, 'Ioana', 'Costache', 'ioana.costache@student.com', 'student', 'Ke1@zXw5');
INSERT INTO public.users VALUES (15, 'Cristian', 'Tudor', 'cristian.tudor@student.com', 'student', '9rT@eL6w');
INSERT INTO public.users VALUES (16, 'Elena', 'Popa', 'elena.popa@student.com', 'student', 'Px2$Mn7q');
INSERT INTO public.users VALUES (17, 'Daria', 'Marin', 'daria.marin@student.com', 'student', 'Jk8&vB3!');
INSERT INTO public.users VALUES (18, 'Alex', 'Dobre', 'alex.dobre@student.com', 'student', 'Xv2!aNp7');
INSERT INTO public.users VALUES (19, 'Bianca', 'Stan', 'bianca.stan@student.com', 'student', 'Gq9$Lm3z');
INSERT INTO public.users VALUES (20, 'Florin', 'Matei', 'florin.matei@student.com', 'student', 'Uz4#Rt2k');
INSERT INTO public.users VALUES (21, 'Teodora', 'Ilie', 'teodora.ilie@student.com', 'student', 'Mv6@Xp9q');
INSERT INTO public.users VALUES (22, 'Andreea', 'Voicu', 'andreea.voicu@student.com', 'student', 'Jt7!Pw8e');
INSERT INTO public.users VALUES (23, 'Marius', 'Serban', 'marius.serban@student.com', 'student', 'Rb3^Yo6l');
INSERT INTO public.users VALUES (24, 'Catalin', 'Petrescu', 'catalin.petrescu@student.com', 'student', 'Np5*Zx2q');
INSERT INTO public.users VALUES (25, 'Gabriela', 'Nistor', 'gabriela.nistor@student.com', 'student', 'Lk8@Rn5w');
INSERT INTO public.users VALUES (26, 'Ionut', 'Mihaila', 'ionut.mihaila@student.com', 'student', 'Vd3!Tp7z');
INSERT INTO public.users VALUES (27, 'Roxana', 'Ciobanu', 'roxana.ciobanu@student.com', 'student', 'Qe6#Uy4p');
INSERT INTO public.users VALUES (28, 'Diana', 'Barbu', 'diana.barbu@student.com', 'student', 'Wm2@Jq9x');
INSERT INTO public.users VALUES (29, 'George', 'Preda', 'george.preda@student.com', 'student', 'Ef9$Kr6b');
INSERT INTO public.users VALUES (30, 'Alina', 'Toma', 'alina.toma@student.com', 'student', 'Tf1^Xp8m');
INSERT INTO public.users VALUES (31, 'Paul', 'Nedelea', 'paul.nedelea@student.com', 'student', 'Yr4!Bm3v');
INSERT INTO public.users VALUES (32, 'Raluca', 'Stefan', 'raluca.stefan@student.com', 'student', 'Zo7@Wt5q');
INSERT INTO public.users VALUES (33, 'Sorin', 'Manole', 'sorin.manole@student.com', 'student', 'Hm5#Lz9k');
INSERT INTO public.users VALUES (34, 'Carmen', 'Olteanu', 'carmen.olteanu@student.com', 'student', 'Pn6!Xv2e');
INSERT INTO public.users VALUES (35, 'Dan', 'Voinea', 'dan.voinea@student.com', 'student', 'Jx8@Wc4r');
INSERT INTO public.users VALUES (36, 'Simona', 'Dinu', 'simona.dinu@student.com', 'student', 'Bt3#Mq7s');
INSERT INTO public.users VALUES (37, 'Ovidiu', 'Rusu', 'ovidiu.rusu@student.com', 'student', 'Ke2!Zn8l');
INSERT INTO public.users VALUES (38, 'Cristiano', 'Ronaldo', 'Ronaldo_goat@gmail.com', 'student', 'pessi');
INSERT INTO public.users VALUES (39, 'Ionel', 'Messi', 'Messiuuuu@gmail.com', 'student', 'Georgina');
INSERT INTO public.users VALUES (40, 'Ilinca', 'Badea', 'ilinca.badea@student.com', 'student', 'Wx4!Tu9m');
INSERT INTO public.users VALUES (41, 'Lucian', 'Zamfir', 'lucian.zamfir@student.com', 'student', 'Ky7@Zn3e');
INSERT INTO public.users VALUES (42, 'Natalia', 'Popescu', 'natalia.popescu@student.com', 'student', 'Qs9#Pt4l');
INSERT INTO public.users VALUES (43, 'Andrei', 'Voiculescu', 'andrei.voiculescu@student.com', 'student', 'Df3^Jm8r');
INSERT INTO public.users VALUES (44, 'Camelia', 'Frunza', 'camelia.frunza@student.com', 'student', 'Rz6@Nx5v');
INSERT INTO public.users VALUES (45, 'Ciprian', 'Ignat', 'ciprian.ignat@student.com', 'student', 'Mg1!Uz6p');
INSERT INTO public.users VALUES (46, 'Silvia', 'Balan', 'silvia.balan@student.com', 'student', 'Tv8$Ky2m');
INSERT INTO public.users VALUES (47, 'Sebastian', 'Fodor', 'sebastian.fodor@student.com', 'student', 'Xn5#Lo3t');
INSERT INTO public.users VALUES (48, 'Ioan', 'Gavrila', 'ioan.gavrila@student.com', 'student', 'Vz2^Hr9k');
INSERT INTO public.users VALUES (49, 'Oana', 'Calin', 'oana.calin@student.com', 'student', 'Jk6!Mt7w');
INSERT INTO public.users VALUES (50, 'Denisa', 'Baciu', 'denisa.baciu@student.com', 'student', 'Fg3@Yv5q');
INSERT INTO public.users VALUES (51, 'Rares', 'Grigore', 'rares.grigore@student.com', 'student', 'Lp7#Qu8z');
INSERT INTO public.users VALUES (52, 'Larisa', 'Tanase', 'larisa.tanase@student.com', 'student', 'Qm9!Wz6n');
INSERT INTO public.users VALUES (53, 'Dragoș', 'Popa', 'dragos.popa@student.com', 'student', 'Uz4^Kt2b');
INSERT INTO public.users VALUES (54, 'Anca', 'Neagu', 'anca.neagu@student.com', 'student', 'Ep6!Zj3x');
INSERT INTO public.users VALUES (55, 'Mihnea', 'Paraschiv', 'mihnea.paraschiv@student.com', 'student', 'Xv2@Nt6e');
INSERT INTO public.users VALUES (56, 'Sorina', 'Lungu', 'sorina.lungu@student.com', 'student', 'Tf8$Mw7q');
INSERT INTO public.users VALUES (57, 'Adrian', 'Barca', 'adrian.barca@student.com', 'student', 'Yq1!Vr5z');
INSERT INTO public.users VALUES (58, 'Corina', 'Andrei', 'corina.andrei@student.com', 'student', 'Lz5#Tp9v');
INSERT INTO public.users VALUES (59, 'Tudor', 'Marinescu', 'tudor.marinescu@student.com', 'student', 'Mk6@Yc3p');
INSERT INTO public.users VALUES (60, 'Ileana', 'Stroe', 'ileana.stroe@student.com', 'student', 'Kf2$Rz8x');
INSERT INTO public.users VALUES (61, 'Victor', 'Mocanu', 'victor.mocanu@student.com', 'student', 'Wm7^Jp4s');
INSERT INTO public.users VALUES (62, 'Daniela', 'Cojocaru', 'daniela.cojocaru@student.com', 'student', 'Gn3!Lx6r');
INSERT INTO public.users VALUES (63, 'Adrian', 'Iftenie', 'adrian.iftenie@profesor.com', 'profesor', 'Pf9@Yu3r');
INSERT INTO public.users VALUES (64, 'Alexandru', 'Ionita', 'alexandru.ionita@profesor.com', 'profesor', 'Vt3#Lk7p');
INSERT INTO public.users VALUES (65, 'Gabriel', 'Constantinescu', 'gabriel.constantinescu@profesor.com', 'profesor', 'Mn6!Qw4z');
INSERT INTO public.users VALUES (66, 'Florin', 'Olariu', 'florin.olariu@profesor.com', 'profesor', 'Rz5@Xp9v');
INSERT INTO public.users VALUES (67, 'Mihai', 'Dinca', 'mihai.dinca@profesor.com', 'profesor', 'Tk2!Vm7q');
INSERT INTO public.users VALUES (68, 'Andreea', 'Nistor', 'andreea.nistor@profesor.com', 'profesor', 'Jx7$Qr8m');
INSERT INTO public.users VALUES (69, 'Cristina', 'Lazar', 'cristina.lazar@profesor.com', 'profesor', 'Wm4#Uy2e');
INSERT INTO public.users VALUES (70, 'Radu', 'Iliescu', 'radu.iliescu@profesor.com', 'profesor', 'Ep3!Vn5k');
INSERT INTO public.users VALUES (71, 'Murinho', 'special_one', 'pressure000@profesor.com', 'profesor', 'Ht6@Zp3l');
INSERT INTO public.users VALUES (72, 'El', 'Professor', 'lacasadepappel@profesor.com', 'profesor', 'Yq9$Tr6x');

-- Enable the pgcrypto extension if not already enabled
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Update the users table to encode passwords using the crypt() function
UPDATE public.users
SET password = crypt(password, gen_salt('bf'));


--
-- Name: answerfc_answerid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.answerfc_answerid_seq', 8, true);


--
-- Name: course_courseid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.course_courseid_seq', 40, true);


--
-- Name: flashcard_flashcardid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.flashcard_flashcardid_seq', 8, true);


--
-- Name: flashcardsession_sessionid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.flashcardsession_sessionid_seq', 8, true);


--
-- Name: material_index_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.material_index_seq', 11, true);


--
-- Name: material_materialid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.material_materialid_seq', 11, true);


--
-- Name: streak_streakid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.streak_streakid_seq', 4, true);


--
-- Name: test_testid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.test_testid_seq', 3, true);


--
-- Name: testanswer_answerid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.testanswer_answerid_seq', 28, true);


--
-- Name: testquestion_answerid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.testquestion_answerid_seq', 9, true);


--
-- Name: testquestion_questionid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.testquestion_questionid_seq', 9, true);


--
-- Name: users_userid_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.users_userid_seq', 72, true);


--
-- Name: answerfc answerfc_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.answerfc
    ADD CONSTRAINT answerfc_pkey PRIMARY KEY (answerid);


--
-- Name: course course_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (courseid);


--
-- Name: enrollment enrollment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.enrollment
    ADD CONSTRAINT enrollment_pkey PRIMARY KEY (userid, courseid);


--
-- Name: flashcard flashcard_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.flashcard
    ADD CONSTRAINT flashcard_pkey PRIMARY KEY (flashcardid);


--
-- Name: flashcardsession flashcardsession_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.flashcardsession
    ADD CONSTRAINT flashcardsession_pkey PRIMARY KEY (sessionid);


--
-- Name: grade grade_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.grade
    ADD CONSTRAINT grade_pkey PRIMARY KEY (testid, userid);


--
-- Name: material material_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.material
    ADD CONSTRAINT material_pkey PRIMARY KEY (index);


--
-- Name: streak streak_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.streak
    ADD CONSTRAINT streak_pkey PRIMARY KEY (streakid);


--
-- Name: test test_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.test
    ADD CONSTRAINT test_pkey PRIMARY KEY (testid);


--
-- Name: testanswer testanswer_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.testanswer
    ADD CONSTRAINT testanswer_pkey PRIMARY KEY (answerid);


--
-- Name: testquestion testquestion_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.testquestion
    ADD CONSTRAINT testquestion_pkey PRIMARY KEY (questionid);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (userid);


--
-- Name: answerfc answerfc_flashcardid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.answerfc
    ADD CONSTRAINT answerfc_flashcardid_fkey FOREIGN KEY (flashcardid) REFERENCES public.flashcard(flashcardid) ON DELETE CASCADE;


--
-- Name: course course_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(userid) ON DELETE CASCADE;


--
-- Name: enrollment enrollment_courseid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.enrollment
    ADD CONSTRAINT enrollment_courseid_fkey FOREIGN KEY (courseid) REFERENCES public.course(courseid) ON DELETE CASCADE;


--
-- Name: enrollment enrollment_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.enrollment
    ADD CONSTRAINT enrollment_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(userid) ON DELETE CASCADE;


--
-- Name: flashcardsession fk2a9to8w64cp2c6nxl6an15hek; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.flashcardsession
    DROP CONSTRAINT IF EXISTS fk2a9to8w64cp2c6nxl6an15hek;


--
-- Name: flashcard fk87hmwh0f1cur5kscdddt777sw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.flashcard
    ADD CONSTRAINT fk87hmwh0f1cur5kscdddt777sw FOREIGN KEY (materialid) REFERENCES public.material(index);


--
-- Name: testquestion fkfvkfjourtdwrr61q4hwsrkj0h; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.testquestion
    ADD CONSTRAINT fkfvkfjourtdwrr61q4hwsrkj0h FOREIGN KEY (questionid) REFERENCES public.testquestion(questionid);


--
-- Name: flashcard flashcard_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.flashcard
    ADD CONSTRAINT flashcard_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(userid) ON DELETE CASCADE;


--
-- Name: flashcardsession flashcardsession_courseid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.flashcardsession
    ADD CONSTRAINT flashcardsession_courseid_fkey FOREIGN KEY (courseid) REFERENCES public.course(courseid) ON DELETE CASCADE;


--
-- Name: flashcardsession flashcardsession_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.flashcardsession
    ADD CONSTRAINT flashcardsession_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(userid) ON DELETE CASCADE;


--
-- Name: grade grade_testid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.grade
    ADD CONSTRAINT grade_testid_fkey FOREIGN KEY (testid) REFERENCES public.test(testid) ON DELETE CASCADE;


--
-- Name: grade grade_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.grade
    ADD CONSTRAINT grade_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(userid) ON DELETE CASCADE;


--
-- Name: material material_courseid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.material
    ADD CONSTRAINT material_courseid_fkey FOREIGN KEY (courseid) REFERENCES public.course(courseid) ON DELETE CASCADE;


--
-- Name: streak streak_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.streak
    ADD CONSTRAINT streak_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(userid) ON DELETE CASCADE;


--
-- Name: test test_courseid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.test
    ADD CONSTRAINT test_courseid_fkey FOREIGN KEY (courseid) REFERENCES public.course(courseid) ON DELETE CASCADE;


--
-- Name: test test_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.test
    ADD CONSTRAINT test_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(userid) ON DELETE CASCADE;


--
-- Name: testanswer testanswer_questionid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.testanswer
    ADD CONSTRAINT testanswer_questionid_fkey FOREIGN KEY (questionid) REFERENCES public.testquestion(questionid) ON DELETE CASCADE;


--
-- Name: testquestion testquestion_testid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.testquestion
    ADD CONSTRAINT testquestion_testid_fkey FOREIGN KEY (testid) REFERENCES public.test(testid) ON DELETE CASCADE;


--
-- Drop the flashcardprogress table if it exists
DROP TABLE IF EXISTS flashcardprogress CASCADE;

-- Create the flashcardprogress table
CREATE TABLE public.flashcardprogress (
    flashcardprogressid bigint NOT NULL,
    userid integer NOT NULL,
    flashcardid bigint NOT NULL,
    easefactor double precision NOT NULL,
    interval integer NOT NULL,
    repetitions integer NOT NULL,
    duedate date NOT NULL,
    lastreviewed timestamp without time zone NOT NULL
);

-- Create sequence for the primary key
CREATE SEQUENCE public.flashcardprogress_flashcardprogressid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Link the sequence to the primary key column
ALTER SEQUENCE public.flashcardprogress_flashcardprogressid_seq OWNED BY public.flashcardprogress.flashcardprogressid;

-- Set default value for primary key to use the sequence
ALTER TABLE ONLY public.flashcardprogress ALTER COLUMN flashcardprogressid SET DEFAULT nextval('public.flashcardprogress_flashcardprogressid_seq'::regclass);

-- Add primary key constraint
ALTER TABLE ONLY public.flashcardprogress
    ADD CONSTRAINT flashcardprogress_pkey PRIMARY KEY (flashcardprogressid);

-- Add foreign key constraints
ALTER TABLE ONLY public.flashcardprogress
    ADD CONSTRAINT flashcardprogress_userid_fkey FOREIGN KEY (userid) REFERENCES public.users(userid) ON DELETE CASCADE;

ALTER TABLE ONLY public.flashcardprogress
    ADD CONSTRAINT flashcardprogress_flashcardid_fkey FOREIGN KEY (flashcardid) REFERENCES public.flashcard(flashcardid) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--
