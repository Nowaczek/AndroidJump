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
    public class LastTicketsController : ApiController
    {
        //
        // GET: /lastTickets/
        //tego nie trzeba robić
        /*public HttpResponseMessage Post(Tocken tocken)
        {
            if (!this.ModelState.IsValid)
            {
                //tutaj wywala ogolny blad gdy otrzymal zle dane
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.BadRequest));
            }

            
            List<ticketLast> lista_biletow = new List<ticketLast>();
            

            //tutaj dodaj do listy obiekt ticket Last co ma name ( czyli nazwe biletu) i time ( do kiedy był ważny w formacie rrrr-mm-dd HH:mm:ss)

            //a jak nei ma w historii biletow to daj do listy jeden obiekt tego typu z name "brak kupionych" time = "biletow"



            HttpResponseMessage response = this.Request.CreateResponse(HttpStatusCode.Created, lista_biletow);

            return response;
        }*/

    }
}
