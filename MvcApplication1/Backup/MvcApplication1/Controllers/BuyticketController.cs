using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;
using MvcApplication1.Models;

namespace MvcApplication1.Controllers
{
    public class BuyticketController : ApiController
    {
     
        public HttpResponseMessage Buy(TicketBuy person)
        {
            if (!this.ModelState.IsValid)
            {
                //tutaj wywala ogolny blad gdy otrzymal zle dane
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.BadRequest));
            }

            loginAnswer reply = new loginAnswer { ok = 0, tocken = null };

            //jezeli wszystko gra, jest siano itp
            //to ok =0 , tocken tez moze byc random
            //jak nie udalo sie to ok na 1 i string

            //todo
            //dodaje bilet o nazwie, dla uzytkownika o tym tokenie
            //UWAGA zalazylem ze nazwa biletu jest w person.Name

            if (Zapytania.kupBilet(person.Tocken, person.Name) == true)
            {
                reply.ok = 0;
            }
            else
            {
                reply.ok = 1;//blad
            }

            HttpResponseMessage response = this.Request.CreateResponse(HttpStatusCode.Created, reply);

            return response;
        }

    }
}
