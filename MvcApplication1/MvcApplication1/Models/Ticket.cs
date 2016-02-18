using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace MvcApplication1
{
    public class Ticket
    {
        public int Id { get; set; }
        public string name { get; set; }
        public DateTime date { get; set; }
        public double price { get; set; } 
    }
}