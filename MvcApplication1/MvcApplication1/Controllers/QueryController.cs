/*using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace MvcApplication1.Controllers
{
    public class QueryController : ApiController
    {
    }
}
*/
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Data;
using System.Data.Entity;

namespace MvcApplication1.Controllers
{
    public class QueryController : ApiController
    {
        public string[] Get()
        {
            string[] tab = null;
            int size;
            try
            {
                using (DB_9EF162_projektpz0001 baza = new DB_9EF162_projektpz0001())
                {
                    var tab_dzialy = (from klient in baza.Kliencis
                                      select new
                                      {
                                          imie = klient.Imie,
                                          nazwisko = klient.Nazwisko,
                                          saldo = klient.Saldo,
                                          id=klient.Id_Klienta
                                      }).ToList();

                   int liczba_pól = 4;
                    size = tab_dzialy.Count()*liczba_pól;
                    tab = new string[size];
                 
                    
                    for (int i = 0; i < size-1; i+=liczba_pól )
                    {
                        tab[i] = tab_dzialy[i / liczba_pól].imie;
                        tab[i+1] = tab_dzialy[i / liczba_pól].nazwisko;
                        tab[i+2] = tab_dzialy[i / liczba_pól].saldo.ToString();
                        tab[i + 3] = tab_dzialy[i / liczba_pól].id.ToString();
                    }
                       /* tab[0] = tab_dzialy[0].imie;
                    tab[1] = tab_dzialy[0].nazwisko;
                    tab[2] = tab_dzialy[1].imie;
                    tab[3] = tab_dzialy[2].saldo;
                    */
                }
            }
            catch (Exception ex)
            {
                tab = null;
            }

            return tab;
        }//*/
    }
}