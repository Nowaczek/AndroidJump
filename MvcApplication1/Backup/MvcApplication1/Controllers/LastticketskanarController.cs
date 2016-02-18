using System.Net;
using System.Net.Http;
using System.Web.Http;
using MvcApplication1.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace MvcApplication1.Controllers
{
    public class LastticketskanarController : ApiController
    {
        //
        // GET: /lastticketskanar/
        public HttpResponseMessage Post(kanarTicket ticket)
        {
            if (!this.ModelState.IsValid)
            {
                //tutaj wywala ogolny blad gdy otrzymal zle dane
                throw new HttpResponseException(Request. CreateResponse(HttpStatusCode.BadRequest));
            }

            List<Ticket> biletyList = new List<Ticket>();
            

            if (Zapytania.ostatnieBiletyUzytkownika(ticket.tocken, ticket.name, ref biletyList) == true)
            {
                //wszystko dobrze poszlo
            }
            else
            {
                biletyList = null;
            }


            HttpResponseMessage response = this.Request.CreateResponse(HttpStatusCode.Created, biletyList);

            return response;
        }

    }
}
