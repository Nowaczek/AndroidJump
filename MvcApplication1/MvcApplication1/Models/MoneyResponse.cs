using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace MvcApplication1.Models
{
    public class MoneyResponse
    {
        public int ok { get; set; }
        public double currentMoney { get; set; }

        //ok = 0 to ma saldo, ok=1 blad ogolny ok= 3 brak srodkow na koncie
        
    }
}