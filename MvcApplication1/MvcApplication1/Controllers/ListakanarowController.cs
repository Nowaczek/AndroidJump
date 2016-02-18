using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace MvcApplication1.Controllers
{
    public class ListakanarowController : ApiController
    {
        public string[] Get()
        {
            string[] tab = null;
            int size;
            try
            {
                using (DB_9EF162_projektpz0001 baza = new DB_9EF162_projektpz0001())
                {
                    var tab_dzialy = (from klient in baza.Kontrolerzies
                        select new
                        {
                            imie = klient.Imię,
                            nazwisko = klient.Nazwisko,
                            login = klient.Login,
                            haslo = klient.Haslo,
                            id = klient.Id_Kontrolera

                        }).ToList();

                    int liczba_pól = 5;
                    size = tab_dzialy.Count() * liczba_pól;
                    tab = new string[size];


                    for (int i = 0; i < size - 1; i += liczba_pól)
                    {
                        tab[i] = tab_dzialy[i / liczba_pól].imie;
                        tab[i + 1] = tab_dzialy[i / liczba_pól].nazwisko;
                        tab[i + 2] = tab_dzialy[i / liczba_pól].login;
                        tab[i + 3] = tab_dzialy[i/liczba_pól].haslo;
                        tab[i + 4] = tab_dzialy[i/liczba_pól].id.ToString();
                    }
                }
            }
            catch (Exception ex)
            {
                tab = null;
            }

            return tab;
        }//*/
    }//class
}
