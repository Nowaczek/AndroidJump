using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace MvcApplication1.Controllers
{
    public class AddTicketKindController : ApiController
    {
        // POST api/logout
        public HttpResponseMessage Post(Ticket bilet)
        {
            if (!this.ModelState.IsValid)
            {
                //tutaj wywala ogolny blad gdy otrzymal zle dane
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.BadRequest));
            }

            loginAnswer reply = new loginAnswer { ok = 0, tocken = null };

            if (Zapytania.dodajBilet(bilet.name, bilet.price, bilet.date) == true)
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
