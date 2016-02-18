using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace MvcApplication1.Controllers
{
    public class UsunklientaController : ApiController
    {
        public HttpResponseMessage Post(int idKlienta)
        {
            if (!this.ModelState.IsValid)
            {
                //tutaj wywala ogolny blad gdy otrzymal zle dane
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.BadRequest));
            }

            bool ok = Zapytania.usunKlienta(idKlienta);

            HttpResponseMessage response = this.Request.CreateResponse(HttpStatusCode.Created, ok);

            return response;
        }
    }
}
