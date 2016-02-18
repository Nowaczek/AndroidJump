using MvcApplication1.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace MvcApplication1.Controllers
{
    public class DodajKanaraController : ApiController
    {
        // POST api/logout
        public HttpResponseMessage Post(Kontroler kanar)
        {
            if (!this.ModelState.IsValid)
            {
                //tutaj wywala ogolny blad gdy otrzymal zle dane
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.BadRequest));
            }

            loginAnswer reply = new loginAnswer { ok = 0, tocken = null };

            if (Zapytania.dodaj_kanara(kanar.imie,kanar.nazwisko,kanar.login,kanar.haslo) == true)
            {
                reply.tocken = "ok";
            }
            else
            {
                reply.tocken = "bad";
            }

            HttpResponseMessage response = this.Request.CreateResponse(HttpStatusCode.Created, reply);

            return response;
        }
    }
}
