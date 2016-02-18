using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;
using MvcApplication1.Models;


using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;
using System.Web.UI.WebControls;
using MvcApplication1.Models;
using WebGrease.Css.Ast.Selectors;


namespace MvcApplication1.Controllers
{
    public class CurrentController : ApiController
    {
        //{
        //    if (!this.ModelState.IsValid)
        //    {
        //        //tutaj wywala ogolny blad gdy otrzymal zle dane
        //        throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.BadRequest));
        //    }

        //    currentTicketAnswer reply = new currentTicketAnswer { ok = 0, whatTicket = "nazwa231",timeTicket = "czas"};

        //    //ok = 0 ma bilet what ticket ma nazwe tego biletu a time ticket to czas biletu w stringu i w danym formacie 2015:05:22:22:59:00 ( rok,mieisac,dzien,godz,min,sec) czyli do kiedy jest bilet
        //    //ok = 1 to brak biletu i brak aktualnego biletu

        //    //string nazwa = "";
        //    //string DO = "";
        //    //if (Zapytania.czyAktualnyBilet(tocken.tocken, ref nazwa, ref DO) == true)
        //    //{
        //    //    reply.whatTicket = nazwa;
        //    //    reply.timeTicket = DO;
        //    //    reply.ok = 0;
        //    //}
        //    //else
        //    //{
        //    //    reply.ok = 1; //blad
        //    //    reply.whatTicket = "zly";
        //    //}

        //    reply.whatTicket = tocken.tocken;
        //    HttpResponseMessage response = this.Request.CreateResponse(HttpStatusCode.Created, reply);

        //    return response;
        //}

        //
        // GET: /current/
        /*
        public HttpResponseMessage Ticket(Tocken person)
        {
            if (!this.ModelState.IsValid)
            {
                //tutaj wywala ogolny blad gdy otrzymal zle dane
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.BadRequest));

            }

            currentTicketAnswer reply = new currentTicketAnswer();

            string jaki_bilet = "blad";
            int czas = -5;

            if (Zapytania.znajdz_bilet(person.tocken, ref jaki_bilet, ref czas) == true)
            {
                reply.ok = 0;
                reply.whatTicket = jaki_bilet;
                reply.timeTicket = czas.ToString();//tu moze byc blad
            }
            else
            {
                reply.ok = 1;
            }

            HttpResponseMessage response = this.Request.CreateResponse(HttpStatusCode.Created, reply);

            return response;
        }
        */
    }
}
