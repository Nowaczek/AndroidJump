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
    public class TakemoneyController : ApiController
    {
        //
        // GET: /takemoney/
        // POST api/logout
        public HttpResponseMessage Post(Tocken tocken)
        {

            if (!this.ModelState.IsValid)
            {
                //tutaj wywala ogolny blad gdy otrzymal zle dane
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.BadRequest));
            }

            MoneyResponse reply = new MoneyResponse{ok=0,currentMoney = 0.0};

            //pobierz saldo kolesia z takim tocken.tocken wpisz w money
            //bledy ok masz opisane w MoneyResponse
            //todo
            //sciaga stan konta
            double stanKonta = -1;

            //pobiera stan konta z bazy danych na podstawie tokena
            if (Zapytania.pobierzStanKonta(tocken.tocken, ref stanKonta) == true)
            {
                reply.ok = 0;
                reply.currentMoney = stanKonta;
            }
            else
            {
                reply.ok = 1;
            }

            HttpResponseMessage response = this.Request.CreateResponse(HttpStatusCode.Created, reply);

            return response;
        }

    }
}
