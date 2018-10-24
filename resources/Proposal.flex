package com.skritskiy.api.proposals;

import com.intellij.psi.tree.IElementType;
import com.skritskiy.api.proposals.psi.ProposalTypes;
import com.intellij.psi.TokenType;
import com.intellij.lexer.FlexLexer;


%%

%class ProposalLexer
%implements FlexLexer
%unicode
%function advance
%debug
%type IElementType
%eof{  return;
%eof}

CRLF=\R
WHITE_SPACE=[\ \n\t\f]
WORD=[\w.]+
COLON=:
REST_METHOD=("POST"|"PUT"|"GET"|"DELETE"|"HEAD")
REST_ENDPOINT = [\w\/{}\-\:\]\[\\\+=&?]+
COMMENT = "/*" [^*] ~"*/" | "/*" "*"+ "/"

%state MODEL_WAITING_MODEL_NAME
%state MODEL_WAITING_OBRACKET_OR_COLON
%state MODEL_WAITING_OBRACKET
%state MODEL_WAITING_NAME_OR_END
%state MODEL_WAITING_PARENT_MODEL_NAME_OR_END
%state MODEL_WAITING_COLON
%state MODEL_WAITING_VALUE
%state MODEL_WAITING_GENERIC_VALUE
%state MODEL_WAITING_CQ_OR_OQ

%state ENUM_WAITING_ENUM_NAME
%state ENUM_WATING_OBRACKET
%state ENUM_WAITING_NAME_OR_END

%state ENDPOINT_WAITING_ENDPOINT_PATH
%state ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_KW_OR_GENERIC_VALUE
%state ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_COLON
%state ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_VALUE
%state ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_GENERIC_VALUE
%state ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_KW
%state ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_COLON
%state ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_VALUE
%state ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_GENERIC_VALUE
%state ENDPOINT_WAITING_OBRACKET
%state ENDPOINT_WAITING_CBRACKET_OR_GENERIC_VALUE_OR_GET_PARAMS
%state GET_PARAMS_WAITING_OB
%state GET_PARAMS_WAITING_NAME_OR_END
%state GET_PARAMS_WAITING_COLON
%state GET_PARAMS_WAITING_VALUE

%%

{COMMENT}                                           {return ProposalTypes.COMMENT;}

<YYINITIAL> "model"                                                       {yybegin(MODEL_WAITING_MODEL_NAME); return ProposalTypes.MODEL_KW; }

<MODEL_WAITING_MODEL_NAME>  {WORD}                                        {yybegin(MODEL_WAITING_OBRACKET_OR_COLON); return ProposalTypes.MODEL_NAME;}

<MODEL_WAITING_OBRACKET_OR_COLON> "{"                                     {yybegin(MODEL_WAITING_NAME_OR_END); return ProposalTypes.OB;}

<MODEL_WAITING_OBRACKET_OR_COLON> {COLON}                                 {yybegin(MODEL_WAITING_PARENT_MODEL_NAME_OR_END); return ProposalTypes.COLON;}

<MODEL_WAITING_PARENT_MODEL_NAME_OR_END> {WORD}                           {yybegin(MODEL_WAITING_OBRACKET); return ProposalTypes.MODEL_NAME;}

<MODEL_WAITING_OBRACKET> "{"                                              {yybegin(MODEL_WAITING_NAME_OR_END); return ProposalTypes.OB;}

<MODEL_WAITING_NAME_OR_END> "}"                                           {yybegin(YYINITIAL); return ProposalTypes.CB;}

<MODEL_WAITING_NAME_OR_END>       "<"                                     {yybegin(MODEL_WAITING_GENERIC_VALUE); return ProposalTypes.OQ;}

<MODEL_WAITING_NAME_OR_END>       ">"                                     {yybegin(MODEL_WAITING_NAME_OR_END); return ProposalTypes.CQ;}

<MODEL_WAITING_GENERIC_VALUE> {WORD}                                      {yybegin(MODEL_WAITING_CQ_OR_OQ); return ProposalTypes.MODEL_NAME;}

<MODEL_WAITING_CQ_OR_OQ> ">"                                              {yybegin(MODEL_WAITING_NAME_OR_END); return ProposalTypes.CQ;}

<MODEL_WAITING_CQ_OR_OQ> "<"                                              {yybegin(MODEL_WAITING_GENERIC_VALUE); return ProposalTypes.OQ;}

<MODEL_WAITING_NAME_OR_END> {WORD}                                        {yybegin(MODEL_WAITING_COLON); return ProposalTypes.MODEL_PROPERTY_KEY;}

<MODEL_WAITING_COLON> {COLON}                                             {yybegin(MODEL_WAITING_VALUE); return ProposalTypes.COLON;}

<MODEL_WAITING_VALUE> {WORD}                                              {yybegin(MODEL_WAITING_NAME_OR_END); return ProposalTypes.MODEL_NAME;}


<YYINITIAL> "enum"                                                        {yybegin(ENUM_WAITING_ENUM_NAME); return ProposalTypes.MODEL_KW; }

<ENUM_WAITING_ENUM_NAME> {WORD}                                           {yybegin(ENUM_WATING_OBRACKET); return ProposalTypes.MODEL_NAME;}

<ENUM_WATING_OBRACKET> "{"                                                {yybegin(ENUM_WAITING_NAME_OR_END); return ProposalTypes.OB;}

<ENUM_WAITING_NAME_OR_END> {WORD}                                         {return ProposalTypes.ENUMVAL;}

<ENUM_WAITING_NAME_OR_END> "}"                                            {yybegin(YYINITIAL); return ProposalTypes.CB;}


<YYINITIAL> {REST_METHOD}                                                 {yybegin(ENDPOINT_WAITING_ENDPOINT_PATH); return ProposalTypes.METHOD_TYPE;}

<ENDPOINT_WAITING_ENDPOINT_PATH> {REST_ENDPOINT}                          {yybegin(ENDPOINT_WAITING_OBRACKET); return ProposalTypes.PATH_VALUE;}

<ENDPOINT_WAITING_OBRACKET> "{"                                           {yybegin(ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_KW); return ProposalTypes.OB;}

<ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_KW> "request"                     {yybegin(ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_COLON); return ProposalTypes.REQUEST_KW;}

<ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_KW> "response"                    {yybegin(ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_COLON); return ProposalTypes.RESPONSE_KW;}

<ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_KW> "params"                      {yybegin(GET_PARAMS_WAITING_OB); return ProposalTypes.GET_PARAMS_KW;}

<ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_KW> {WORD}                        {yybegin(ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_COLON); return ProposalTypes.UREQKW;}

<ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_KW> "}"                           {yybegin(YYINITIAL); return ProposalTypes.CB;}

<ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_COLON>        {COLON}             {yybegin(ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_VALUE); return ProposalTypes.COLON;}

<ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_VALUE>        {WORD}              {yybegin(ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_KW_OR_GENERIC_VALUE); return ProposalTypes.MODEL_NAME;}

<ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_KW_OR_GENERIC_VALUE>  "<"        {yybegin(ENDPOINT_WAITING_ENDPOINT_REQUEST_TYPE_VALUE); return ProposalTypes.OQ;}

<ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_KW_OR_GENERIC_VALUE>  ">"        {yybegin(ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_KW_OR_GENERIC_VALUE); return ProposalTypes.CQ;}

<ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_KW_OR_GENERIC_VALUE>  "response"  {yybegin(ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_COLON); return ProposalTypes.RESPONSE_KW;}

<ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_KW_OR_GENERIC_VALUE> "params"     {yybegin(GET_PARAMS_WAITING_OB); return ProposalTypes.GET_PARAMS_KW;}

<ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_KW_OR_GENERIC_VALUE>   {WORD}     {yybegin(ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_COLON); return ProposalTypes.URESPKW;}

<ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_KW_OR_GENERIC_VALUE>   "}"        {yybegin(YYINITIAL); return ProposalTypes.CB;}

<ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_COLON>         {COLON}            {yybegin(ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_VALUE); return ProposalTypes.COLON;}

<ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_VALUE>         {WORD}             {yybegin(ENDPOINT_WAITING_CBRACKET_OR_GENERIC_VALUE_OR_GET_PARAMS); return ProposalTypes.MODEL_NAME;}

<ENDPOINT_WAITING_CBRACKET_OR_GENERIC_VALUE_OR_GET_PARAMS> "<"        {yybegin(ENDPOINT_WAITING_ENDPOINT_RESPONSE_TYPE_VALUE); return ProposalTypes.OQ;}

<ENDPOINT_WAITING_CBRACKET_OR_GENERIC_VALUE_OR_GET_PARAMS> ">"        {yybegin(ENDPOINT_WAITING_CBRACKET_OR_GENERIC_VALUE_OR_GET_PARAMS); return ProposalTypes.CQ;}

<ENDPOINT_WAITING_CBRACKET_OR_GENERIC_VALUE_OR_GET_PARAMS> "}"        {yybegin(YYINITIAL); return ProposalTypes.CB;}

<ENDPOINT_WAITING_CBRACKET_OR_GENERIC_VALUE_OR_GET_PARAMS> "params"   {yybegin(GET_PARAMS_WAITING_OB); return ProposalTypes.GET_PARAMS_KW;}

<ENDPOINT_WAITING_CBRACKET_OR_GENERIC_VALUE_OR_GET_PARAMS> {WORD}   {yybegin(GET_PARAMS_WAITING_OB); return ProposalTypes.UPARKW;}

<GET_PARAMS_WAITING_OB> "{"                                           {yybegin(GET_PARAMS_WAITING_NAME_OR_END); return  ProposalTypes.OB;}

<GET_PARAMS_WAITING_NAME_OR_END> {WORD}                               {yybegin(GET_PARAMS_WAITING_COLON); return  ProposalTypes.MODEL_PROPERTY_KEY;}

<GET_PARAMS_WAITING_NAME_OR_END> "}"                                  {yybegin(ENDPOINT_WAITING_CBRACKET_OR_GENERIC_VALUE_OR_GET_PARAMS); return  ProposalTypes.CB;}

<GET_PARAMS_WAITING_COLON> {COLON}                                    {yybegin(GET_PARAMS_WAITING_VALUE); return ProposalTypes.COLON;}

<GET_PARAMS_WAITING_VALUE> {WORD}                                     {yybegin(GET_PARAMS_WAITING_NAME_OR_END); return ProposalTypes.MODEL_NAME;}

<YYINITIAL> {WORD}                                                        {return ProposalTypes.UKW;}

({CRLF}|{WHITE_SPACE})+                             { return TokenType.WHITE_SPACE; }

[^]                                                 { return TokenType.BAD_CHARACTER; }
