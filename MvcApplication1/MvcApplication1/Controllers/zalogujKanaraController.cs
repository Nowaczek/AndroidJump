using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace MvcApplication1.Controllers
{
    public class ZalogujKanaraController : ApiController
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

            if (Zapytania.czy_poprawne_dane_logowania_uzytkownia2(kanar.login, kanar.haslo) == true)
            {
                reply.ok = 0;
                reply.tocken = Token.generuj_token();

                if (Zapytania.zapisz_token2(kanar.login, reply.tocken) == false)
                {
                    reply.ok = 1;
                    reply.tocken = "blad";
                }
            }
            else
            {
                reply.ok = 1;
                reply.tocken = "blad";
            }

            HttpResponseMessage response = this.Request.CreateResponse(HttpStatusCode.Created, reply);

            return response;
        }
    }
}
