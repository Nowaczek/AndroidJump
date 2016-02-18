using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace MvcApplication1.Controllers
{
    public class DodajKlientController : ApiController
    {
        //
        // GET: /dodajKlient/

        public HttpResponseMessage Login(Kontroler person)
        {
            if (!this.ModelState.IsValid)
            {
                //tutaj wywala ogolny blad gdy otrzymal zle dane
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.BadRequest));
            }

            loginAnswer reply = new loginAnswer { ok = 0, tocken = null };

            if (Zapytania.czyKlientIstnieje(person.login) == false)//tu ma byc czy istnieje
            {
                if (Zapytania.dodajKlienta(person.imie, person.nazwisko, person.login, person.haslo) == true)
                {
                    reply.ok = 0;
                }
                else
                {
                    reply.ok = 1;
                    reply.tocken = "Błąd rejestracji użytkownika.";
                }
            }
            else
            {
                reply.ok = 1;
                reply.tocken = "Użytkownik o podanym loginie już istnieje.";
            }
            //reply.tocken = person.imie + " " + person.nazwisko + " " + person.login + " " + person.haslo;
            //reply.ok = 1;

            HttpResponseMessage response = this.Request.CreateResponse(HttpStatusCode.Created, reply);

            return response;

        }
    }
}
