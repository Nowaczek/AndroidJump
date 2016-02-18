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
    public class CurrentTicketController : ApiController
    {
        //
        // GET: /currentTicket/

        public HttpResponseMessage Post(Tocken tocken)
        {
            if (!this.ModelState.IsValid)
            {
                //tutaj wywala ogolny blad gdy otrzymal zle dane
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.BadRequest));
            }

            //currentTicketAnswer reply = new currentTicketAnswer { ok = 0, whatTicket = "nazwa231",timeTicket = "czas"};

            //ok = 0 ma bilet what ticket ma nazwe tego biletu a time ticket to czas biletu w stringu i w danym formacie 2015:05:22:22:59:00 ( rok,mieisac,dzien,godz,min,sec) czyli do kiedy jest bilet
            //ok = 1 to brak biletu i brak aktualnego biletu

            List<Ticket> lista_biletow = new List<Ticket>();

            if (Zapytania.czyAktualnyBilet(tocken.tocken, ref lista_biletow) == true)
            {
                //reply.ok = 0;
            }
            else
            {
                //reply.ok = 1; //blad
                lista_biletow = null;
            }

            //string nazwa = "";
            //string DO = "";
            //if (Zapytania.czyAktualnyBilet(tocken.tocken, ref nazwa, ref DO) == true)
            //{
            //    reply.whatTicket = nazwa;
            //    reply.timeTicket = DO;
            //    reply.ok = 0;
            //}
            //else
            //{
            //    reply.ok = 1; //blad
            //    //reply.whatTicket = "zly";
            //    reply.timeTicket = (nazwa == "") ? "pusty_string" : nazwa;
            //    reply.whatTicket = (DO == "") ? "token: "+tocken.tocken : DO;
            //}

            HttpResponseMessage response = this.Request.CreateResponse(HttpStatusCode.Created, lista_biletow);

            return response;
        }

    }
}
