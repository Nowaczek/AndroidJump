﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using MvcApplication1.Models;

namespace MvcApplication1.Controllers
{
    public class LogOutController : ApiController
    {
       

        // POST api/logout
        public HttpResponseMessage Post(Tocken tocken)
        {

            if (!this.ModelState.IsValid)
            {
                //tutaj wywala ogolny blad gdy otrzymal zle dane
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.BadRequest));
            }

            if (Zapytania.usun_token(tocken.tocken) == true)
            {
                tocken.tocken = "ok";            
            }
            else
            {
                tocken.tocken = "bad";
            }         

            HttpResponseMessage response = this.Request.CreateResponse(HttpStatusCode.Created,tocken );

            return response;
        }

       
    }
}
