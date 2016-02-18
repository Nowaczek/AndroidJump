using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace MvcApplication1.Controllers
{
    public class DodajKontroleraController : ApiController
    {
        public HttpResponseMessage Post(Kontroler2 kanar)
        {
            if (!this.ModelState.IsValid)
            {
                //tutaj wywala ogolny blad gdy otrzymal zle dane
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.BadRequest));
            }

            loginAnswer reply = new loginAnswer { ok = 0, tocken = null };

            if (Zapytania.dodajKontrolera(kanar.imie, kanar.nazwisko, kanar.umowaOD, kanar.umowaDO, kanar.Login, kanar.Haslo) == true)
            {
                reply.tocken = "ok";
                reply.ok = 0;
            }
            else
            {
                reply.tocken = "bad";
                reply.ok = 1;
            }

            HttpResponseMessage response = this.Request.CreateResponse(HttpStatusCode.Created, reply);

            return response;
        }
    }
}
