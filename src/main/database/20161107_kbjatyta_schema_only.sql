--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.14
-- Dumped by pg_dump version 9.3.14
-- Started on 2016-11-07 10:30:19 PYST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

--
-- TOC entry 171 (class 1259 OID 68281)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: jatyta
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO jatyta;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 172 (class 1259 OID 68283)
-- Name: itemprop; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE itemprop (
    iditemprop bigint NOT NULL,
    comments character varying(255),
    iditemtype bigint NOT NULL,
    idpropname bigint NOT NULL
);


ALTER TABLE public.itemprop OWNER TO jatyta;

--
-- TOC entry 173 (class 1259 OID 68286)
-- Name: itemtype; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE itemtype (
    iditemtype bigint NOT NULL,
    comments character varying(255),
    typename character varying(255),
    idschema bigint
);


ALTER TABLE public.itemtype OWNER TO jatyta;

--
-- TOC entry 174 (class 1259 OID 68292)
-- Name: jatytabrokenlinksconfiguration; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE jatytabrokenlinksconfiguration (
    id bigint NOT NULL,
    configurationid character varying(255) NOT NULL,
    pattern character varying(255) NOT NULL
);


ALTER TABLE public.jatytabrokenlinksconfiguration OWNER TO jatyta;

--
-- TOC entry 175 (class 1259 OID 68298)
-- Name: jatytabrokenstate; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE jatytabrokenstate (
    id bigint NOT NULL,
    idcrawlrecord integer,
    statebroken integer,
    statecaller integer,
    urlbroken character varying(255),
    xpath character varying(255),
    stylevalue character varying(255),
    image bytea
);


ALTER TABLE public.jatytabrokenstate OWNER TO jatyta;

--
-- TOC entry 2153 (class 0 OID 0)
-- Dependencies: 175
-- Name: COLUMN jatytabrokenstate.image; Type: COMMENT; Schema: public; Owner: jatyta
--

COMMENT ON COLUMN jatytabrokenstate.image IS 'Contains the image of the broken link element';


--
-- TOC entry 176 (class 1259 OID 68304)
-- Name: jatytacrawlconfiguration; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE jatytacrawlconfiguration (
    id bigint NOT NULL,
    ajustjsfautogeneratedid boolean NOT NULL,
    configurationid character varying(255) NOT NULL,
    whiteboxtest boolean NOT NULL,
    idschema bigint,
    formvaluesfilter character varying(255),
    maxvaluesforforminput integer,
    formsubmitattribute character varying(255),
    formsubmitattributevalue character varying(255),
    formsubmitelement character varying(255),
    formsubmittext character varying(255),
    maxnumberfieldsize integer,
    maxtextfieldsize integer,
    titleformunderxpath character varying(255),
    datevaluepattern character varying(255),
    loginpasswordvalue character varying(255),
    loginpasswordxpath character varying(255),
    loginsubmitxpath character varying(255),
    loginusernamevalue character varying(255),
    loginusernamexpath character varying(255)
);


ALTER TABLE public.jatytacrawlconfiguration OWNER TO jatyta;

--
-- TOC entry 177 (class 1259 OID 68310)
-- Name: jatytacrawlrecord; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE jatytacrawlrecord (
    id bigint NOT NULL,
    configurationid character varying(255) NOT NULL,
    configurationname character varying(255) NOT NULL,
    createtime timestamp without time zone,
    duration bigint,
    idcrawlrecord integer NOT NULL,
    starttime timestamp without time zone,
    statesnumber integer,
    whiteboxtest boolean
);


ALTER TABLE public.jatytacrawlrecord OWNER TO jatyta;

--
-- TOC entry 178 (class 1259 OID 68316)
-- Name: jatytaformconfiguration; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE jatytaformconfiguration (
    id bigint NOT NULL,
    formsubmitattribute character varying(255),
    formsubmitattributevalue character varying(255),
    formsubmitelement character varying NOT NULL,
    formsubmittext character varying(255),
    configurationid character varying(255) NOT NULL
);


ALTER TABLE public.jatytaformconfiguration OWNER TO jatyta;

--
-- TOC entry 179 (class 1259 OID 68322)
-- Name: jatytaformfieldrecord; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE jatytaformfieldrecord (
    id bigint NOT NULL,
    formfieldxpath character varying(255),
    idcrawlrecord integer,
    image bytea,
    labelimage bytea
);


ALTER TABLE public.jatytaformfieldrecord OWNER TO jatyta;

--
-- TOC entry 2154 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN jatytaformfieldrecord.image; Type: COMMENT; Schema: public; Owner: jatyta
--

COMMENT ON COLUMN jatytaformfieldrecord.image IS 'The image of the web element';


--
-- TOC entry 2155 (class 0 OID 0)
-- Dependencies: 179
-- Name: COLUMN jatytaformfieldrecord.labelimage; Type: COMMENT; Schema: public; Owner: jatyta
--

COMMENT ON COLUMN jatytaformfieldrecord.labelimage IS 'The image of the web element label';


--
-- TOC entry 180 (class 1259 OID 68328)
-- Name: jatytaformvaluerecord; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE jatytaformvaluerecord (
    id bigint NOT NULL,
    formfieldxpath character varying(255) NOT NULL,
    idcrawlrecord integer NOT NULL,
    stateasociated integer NOT NULL,
    type character varying(255) NOT NULL,
    value character varying(2000) NOT NULL,
    idformfieldrecord bigint,
    value_order integer NOT NULL
);


ALTER TABLE public.jatytaformvaluerecord OWNER TO jatyta;

--
-- TOC entry 181 (class 1259 OID 68334)
-- Name: jatytastatename; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE jatytastatename (
    id bigint NOT NULL,
    idcrawlrecord integer NOT NULL,
    url character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    state integer NOT NULL
);


ALTER TABLE public.jatytastatename OWNER TO jatyta;

--
-- TOC entry 182 (class 1259 OID 68340)
-- Name: jatytavalidationconfiguration; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE jatytavalidationconfiguration (
    id bigint NOT NULL,
    attributename character varying(255) NOT NULL,
    attributevalue character varying(255) NOT NULL,
    configurationid character varying(255) NOT NULL,
    htmlelement character varying(255) NOT NULL,
    targetattributename character varying(255)
);


ALTER TABLE public.jatytavalidationconfiguration OWNER TO jatyta;

--
-- TOC entry 183 (class 1259 OID 68346)
-- Name: jatytavalidationrecord; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE jatytavalidationrecord (
    id bigint NOT NULL,
    idcrawlrecord integer NOT NULL,
    targetelementxpath character varying(255),
    targetstate integer,
    validationelementxpath character varying(255),
    validationstate integer NOT NULL,
    validationstatus character varying(255) NOT NULL,
    targetvalue character varying(2000),
    image bytea,
    value_order integer NOT NULL,
    value_type character varying(255) NOT NULL
);


ALTER TABLE public.jatytavalidationrecord OWNER TO jatyta;

--
-- TOC entry 184 (class 1259 OID 68352)
-- Name: nativetype; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE nativetype (
    idnativetype bigint NOT NULL,
    typename character varying(255)
);


ALTER TABLE public.nativetype OWNER TO jatyta;

--
-- TOC entry 185 (class 1259 OID 68355)
-- Name: propname; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE propname (
    idpropname bigint NOT NULL,
    comments character varying(255),
    name character varying(255) NOT NULL,
    idnativetype bigint
);


ALTER TABLE public.propname OWNER TO jatyta;

--
-- TOC entry 186 (class 1259 OID 68361)
-- Name: propvalue; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE propvalue (
    idpropvalues bigint NOT NULL,
    valid boolean,
    value character varying(2000),
    idpropname bigint NOT NULL
);


ALTER TABLE public.propvalue OWNER TO jatyta;

--
-- TOC entry 187 (class 1259 OID 68367)
-- Name: schema; Type: TABLE; Schema: public; Owner: jatyta; Tablespace: 
--

CREATE TABLE schema (
    idschema bigint NOT NULL,
    comments character varying(255),
    schemaname character varying(255),
    isschemaorg boolean
);


ALTER TABLE public.schema OWNER TO jatyta;

--
-- TOC entry 1976 (class 2606 OID 70585)
-- Name: itemprop_pkey; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY itemprop
    ADD CONSTRAINT itemprop_pkey PRIMARY KEY (iditemprop);


--
-- TOC entry 1980 (class 2606 OID 70587)
-- Name: itemtype_pkey; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY itemtype
    ADD CONSTRAINT itemtype_pkey PRIMARY KEY (iditemtype);


--
-- TOC entry 1984 (class 2606 OID 70589)
-- Name: jatytabrokenlinksconfiguration_pkey; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytabrokenlinksconfiguration
    ADD CONSTRAINT jatytabrokenlinksconfiguration_pkey PRIMARY KEY (id);


--
-- TOC entry 1988 (class 2606 OID 70591)
-- Name: jatytabrokenstate_pkey; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytabrokenstate
    ADD CONSTRAINT jatytabrokenstate_pkey PRIMARY KEY (id);


--
-- TOC entry 1990 (class 2606 OID 70593)
-- Name: jatytacrawlconfiguration_pkey; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytacrawlconfiguration
    ADD CONSTRAINT jatytacrawlconfiguration_pkey PRIMARY KEY (id);


--
-- TOC entry 1994 (class 2606 OID 70595)
-- Name: jatytacrawlrecord_pkey; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytacrawlrecord
    ADD CONSTRAINT jatytacrawlrecord_pkey PRIMARY KEY (id);


--
-- TOC entry 1998 (class 2606 OID 70597)
-- Name: jatytaformconfiguration_pk; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytaformconfiguration
    ADD CONSTRAINT jatytaformconfiguration_pk PRIMARY KEY (id);


--
-- TOC entry 2000 (class 2606 OID 70599)
-- Name: jatytaformfieldrecord_pk; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytaformfieldrecord
    ADD CONSTRAINT jatytaformfieldrecord_pk PRIMARY KEY (id);


--
-- TOC entry 2002 (class 2606 OID 70601)
-- Name: jatytaformvaluerecord_pkey; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytaformvaluerecord
    ADD CONSTRAINT jatytaformvaluerecord_pkey PRIMARY KEY (id);


--
-- TOC entry 2004 (class 2606 OID 70603)
-- Name: jatytastatename_pkey; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytastatename
    ADD CONSTRAINT jatytastatename_pkey PRIMARY KEY (id);


--
-- TOC entry 2006 (class 2606 OID 70605)
-- Name: jatytavalidationconfiguration_pkey; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytavalidationconfiguration
    ADD CONSTRAINT jatytavalidationconfiguration_pkey PRIMARY KEY (id);


--
-- TOC entry 2008 (class 2606 OID 70607)
-- Name: jatytavalidationconfiguration_uk; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytavalidationconfiguration
    ADD CONSTRAINT jatytavalidationconfiguration_uk UNIQUE (configurationid, attributename, htmlelement, attributevalue);


--
-- TOC entry 2012 (class 2606 OID 70609)
-- Name: jatytavalidationrecord_pkey; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytavalidationrecord
    ADD CONSTRAINT jatytavalidationrecord_pkey PRIMARY KEY (id);


--
-- TOC entry 2018 (class 2606 OID 70611)
-- Name: nativetype_pkey; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY nativetype
    ADD CONSTRAINT nativetype_pkey PRIMARY KEY (idnativetype);


--
-- TOC entry 2022 (class 2606 OID 70613)
-- Name: propname_pkey; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY propname
    ADD CONSTRAINT propname_pkey PRIMARY KEY (idpropname);


--
-- TOC entry 2026 (class 2606 OID 70615)
-- Name: propvalue_pkey; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY propvalue
    ADD CONSTRAINT propvalue_pkey PRIMARY KEY (idpropvalues);


--
-- TOC entry 2030 (class 2606 OID 70617)
-- Name: schema_pkey; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY schema
    ADD CONSTRAINT schema_pkey PRIMARY KEY (idschema);


--
-- TOC entry 1982 (class 2606 OID 70619)
-- Name: uk_4ysojrac1ybq1nx9wdg34i9ok; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY itemtype
    ADD CONSTRAINT uk_4ysojrac1ybq1nx9wdg34i9ok UNIQUE (typename, idschema);


--
-- TOC entry 2020 (class 2606 OID 70621)
-- Name: uk_85cmjjyevs79i2buoirjffo8t; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY nativetype
    ADD CONSTRAINT uk_85cmjjyevs79i2buoirjffo8t UNIQUE (typename);


--
-- TOC entry 2010 (class 2606 OID 70623)
-- Name: uk_ahchvxcn5ig3hneqhmtj8iacq; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytavalidationconfiguration
    ADD CONSTRAINT uk_ahchvxcn5ig3hneqhmtj8iacq UNIQUE (htmlelement, attributename, configurationid, attributevalue);


--
-- TOC entry 1996 (class 2606 OID 70627)
-- Name: uk_c3ve90d1q7hsrwcvnmt7nyf6k; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytacrawlrecord
    ADD CONSTRAINT uk_c3ve90d1q7hsrwcvnmt7nyf6k UNIQUE (idcrawlrecord);


--
-- TOC entry 2028 (class 2606 OID 70629)
-- Name: uk_cq7rjb3fxumedtg68lpeoyuff; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY propvalue
    ADD CONSTRAINT uk_cq7rjb3fxumedtg68lpeoyuff UNIQUE (value, idpropname);


--
-- TOC entry 1992 (class 2606 OID 70631)
-- Name: uk_cwtjmet5km9s0f5ygj8s4iows; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytacrawlconfiguration
    ADD CONSTRAINT uk_cwtjmet5km9s0f5ygj8s4iows UNIQUE (configurationid);


--
-- TOC entry 2024 (class 2606 OID 70633)
-- Name: uk_hsqkg1x95h1iclfaem7wclnch; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY propname
    ADD CONSTRAINT uk_hsqkg1x95h1iclfaem7wclnch UNIQUE (name);


--
-- TOC entry 2032 (class 2606 OID 70635)
-- Name: uk_misc5bk1l4c147lmobcd69k4a; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY schema
    ADD CONSTRAINT uk_misc5bk1l4c147lmobcd69k4a UNIQUE (schemaname);


--
-- TOC entry 2016 (class 2606 OID 70637)
-- Name: uk_o7iva5qow50jota8100qguu9o; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytavalidationrecord
    ADD CONSTRAINT uk_o7iva5qow50jota8100qguu9o UNIQUE (idcrawlrecord, validationelementxpath, validationstate, targetstate, targetelementxpath, value_order);


--
-- TOC entry 1986 (class 2606 OID 70639)
-- Name: uk_pjklxdbjdoqvuj5p9j33reokq; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY jatytabrokenlinksconfiguration
    ADD CONSTRAINT uk_pjklxdbjdoqvuj5p9j33reokq UNIQUE (configurationid, pattern);


--
-- TOC entry 1978 (class 2606 OID 70641)
-- Name: uk_q79p6t0qm0f7diudupw1387ui; Type: CONSTRAINT; Schema: public; Owner: jatyta; Tablespace: 
--

ALTER TABLE ONLY itemprop
    ADD CONSTRAINT uk_q79p6t0qm0f7diudupw1387ui UNIQUE (idpropname, iditemtype);


--
-- TOC entry 2033 (class 2606 OID 70642)
-- Name: fk_25go5yyqqsq26w3l0tlq86gho; Type: FK CONSTRAINT; Schema: public; Owner: jatyta
--

ALTER TABLE ONLY itemprop
    ADD CONSTRAINT fk_25go5yyqqsq26w3l0tlq86gho FOREIGN KEY (idpropname) REFERENCES propname(idpropname);


--
-- TOC entry 2036 (class 2606 OID 70647)
-- Name: fk_3fuvvy2j3cxar51wvdy608jbi; Type: FK CONSTRAINT; Schema: public; Owner: jatyta
--

ALTER TABLE ONLY jatytacrawlconfiguration
    ADD CONSTRAINT fk_3fuvvy2j3cxar51wvdy608jbi FOREIGN KEY (idschema) REFERENCES schema(idschema);


--
-- TOC entry 2034 (class 2606 OID 70652)
-- Name: fk_e12ts00n6fjv15kki1a82t8wb; Type: FK CONSTRAINT; Schema: public; Owner: jatyta
--

ALTER TABLE ONLY itemprop
    ADD CONSTRAINT fk_e12ts00n6fjv15kki1a82t8wb FOREIGN KEY (iditemtype) REFERENCES itemtype(iditemtype);


--
-- TOC entry 2038 (class 2606 OID 70657)
-- Name: fk_gx7hpxt1744ux2ugsnp92ahm1; Type: FK CONSTRAINT; Schema: public; Owner: jatyta
--

ALTER TABLE ONLY propname
    ADD CONSTRAINT fk_gx7hpxt1744ux2ugsnp92ahm1 FOREIGN KEY (idnativetype) REFERENCES nativetype(idnativetype);


--
-- TOC entry 2035 (class 2606 OID 70662)
-- Name: fk_mablnb81g54mhcvrqsjtua42r; Type: FK CONSTRAINT; Schema: public; Owner: jatyta
--

ALTER TABLE ONLY itemtype
    ADD CONSTRAINT fk_mablnb81g54mhcvrqsjtua42r FOREIGN KEY (idschema) REFERENCES schema(idschema);


--
-- TOC entry 2039 (class 2606 OID 70667)
-- Name: fk_py2isa4s5rfub9jhivm7irtfv; Type: FK CONSTRAINT; Schema: public; Owner: jatyta
--

ALTER TABLE ONLY propvalue
    ADD CONSTRAINT fk_py2isa4s5rfub9jhivm7irtfv FOREIGN KEY (idpropname) REFERENCES propname(idpropname);


--
-- TOC entry 2037 (class 2606 OID 70672)
-- Name: jatytaformvaluerecord_fk; Type: FK CONSTRAINT; Schema: public; Owner: jatyta
--

ALTER TABLE ONLY jatytaformvaluerecord
    ADD CONSTRAINT jatytaformvaluerecord_fk FOREIGN KEY (idformfieldrecord) REFERENCES jatytaformfieldrecord(id);


--
-- TOC entry 2152 (class 0 OID 0)
-- Dependencies: 7
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-11-07 10:30:23 PYST

--
-- PostgreSQL database dump complete
--

