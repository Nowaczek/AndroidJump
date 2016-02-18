using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace MvcApplication1.Controllers
{
    public class TicketsController : ApiController
    {
        public Ticket[] Get()
        {
            //todo
            //lista biletow nazwa i cena (string i double)

            //tutaj chce aby zwracalo taka liste z name tiketu ( czyli jak dlugo i daj cene w nazwie)
            //a cene daj w double, bo ona mi pozwala puscic zakup jezeli mam dostateczne saldo

            List<Ticket> lista_biletow = new List<Ticket>();


            if (Zapytania.pobierzListeBiletow(ref lista_biletow) == false)
            {
                lista_biletow = null;                
            }

            Ticket[] bilety = new Ticket[lista_biletow.Count];

            for (int i = 0; i < lista_biletow.Count; i++)
            {
                bilety[i] = lista_biletow[i];
            }

            return bilety;
            //return new Ticket[]
            //{
            //    new Ticket
            //    {
                    
            //        name = "Glenn Block",
                    
            //        price = 5.5,

            //    },
            //    new Ticket
            //    {
                    
            //        name = "Dan Roth",
                    
            //        price = 6.5
            //    }
            //};
        }
    }
}
