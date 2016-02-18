using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
//using testy_WindowsForms;

namespace MvcApplication1
{
    static class Zapytania
    {
        public static bool czy_poprawne_dane_logowania_uzytkownia(string login, string password)
        {
            //true - tak login i haslo istnieja
            //false - blad logowania lub wyjatek
            bool wynik = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var answer = (from klient in baza.Kliencis
                                  where klient.Login == login
                                  select new
                                  {
                                      haslo = klient.Haslo
                                  }).FirstOrDefault();

                    if (answer != null)
                    {                   
                        if (answer.haslo == password)
                        {
                            wynik = true;
                        }
                        else
                        {
                            wynik = false;
                        } 
                    }
                    else
                    {
                        wynik = false;
                    }
                }
            }
            catch (Exception ex)
            {
                wynik = false;
            }

            return wynik;
        }

        public static bool czy_token_istnieje(string token)
        {
            //true - tak login i haslo istnieja
            //false - blad logowania lub wyjatek
            bool istnieje = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var answer = (from klient in baza.Kliencis
                                  select klient).ToList();

                    if (answer != null)
                    {
                        foreach (var item in answer)
                        {
                            if (item.Token == token)
                            {
                                istnieje = true;
                                break;
                            }
                        }

                        if (istnieje == true)
                        {
                            istnieje = false;
                        } 
                    }
                }
            }
            catch (Exception ex)
            {
                istnieje = false;  //blad serwera
            }

            return istnieje;
        }

        public static bool zapisz_token(string login, string token)
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var answer = (from klient in baza.Kliencis
                                  where klient.Login == login
                                  select klient).ToList();

                    if (answer != null)
                    {
                        //nieoptymalne
                        foreach (var item in answer)
                        {
                            var original = baza.Kliencis.Find(item.Id_Klienta);

                            if (original != null)
                            {
                                original.Token = token;
                                baza.SaveChanges();
                                ok = true;
                            }
                            else
                            {
                                ok = false;
                            }
                        }                        
                    }
                    else
                    {
                        ok = false;
                    }
                }
            }
            catch (Exception ex)
            {
                ok = false;
            }

            return ok;
        }

        public static bool usun_token(string token)
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var answer = (from klient in baza.Kliencis
                                  where klient.Token == token
                                  select klient).ToList();

                    if (answer != null)
                    {
                        //nieoptymalne
                        foreach (var item in answer)
                        {
                            var original = baza.Kliencis.Find(item.Id_Klienta);

                            if (original != null)
                            {
                                original.Token = "usuniety_token";
                                baza.SaveChanges();
                                ok = true;
                            }
                            else
                            {
                                ok = false;
                            }
                        }
                    }
                    else
                    {
                        ok = false;
                    }
                }
            }
            catch (Exception ex)
            {
                ok = false;  //blad serwera
            }

            return ok;
        }

        public static bool znajdz_bilet(string token, ref string nazwa, ref int czas)
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var klient = (from k in baza.Kliencis
                                  where k.Token == token
                                  select k).FirstOrDefault();

                    if (klient != null)
                    {
                        var kupiony_bilet = (from bilet in baza.Zakupione_bilety
                                             where bilet.Id_Klienta == klient.Id_Klienta
                                             select bilet).FirstOrDefault();

                        if (kupiony_bilet != null)
                        {
                            var rodzaj_biletu = (from rodzaj in baza.Rodzaje_biletów
                                                 where rodzaj.Id_Rodzaju == kupiony_bilet.Id_Rodzaju
                                                 select rodzaj).FirstOrDefault();

                            if (rodzaj_biletu != null)
                            {
                                DateTime wazny_do = kupiony_bilet.Ważność_DO;
                                DateTime aktualny_czas = DateTime.UtcNow;
                                TimeSpan ts = new TimeSpan();

                                ts = wazny_do - aktualny_czas;
                                double x = (int)ts.TotalSeconds;

                                ok = true;

                                if (x > 0.0)
                                {
                                    ok = true;
                                    czas = (int)x;
                                    nazwa = rodzaj_biletu.Nazwa;
                                }
                                else
                                {
                                    ok = false;
                                }
                            }
                            else
                            {
                                ok = false;
                            }                            
                        }
                        else
                        {
                            ok = false;
                        }                        
                    }
                    else
                    {
                        ok = false;
                    }
                }
            }
            catch (Exception ex)
            {
                ok = false;  //blad serwera
            }

            return ok;
        }//znajdz bilet

        public static bool dodaj_kanara(string imie, string nazwisko, string login, string haslo)
        {
            //trzeba sprawdzic czy login jest unikalny jesli tak to mozna dodac
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var klient = (from k in baza.Kontrolerzies
                                   where k.Login == login
                                  select k).FirstOrDefault();

                    if (klient == null)//jak nie ma to dobrze
                    {
                        Kontrolerzy nowy_klient = new Kontrolerzy();
                        nowy_klient.Imię = imie;
                        nowy_klient.Nazwisko = nazwisko;
                        nowy_klient.Login = login;
                        nowy_klient.Haslo = haslo;

                        baza.Kontrolerzies.Add(nowy_klient);
                        baza.SaveChanges();

                        ok = true;
                    }
                    else
                    {
                        return false;
                    }

                }
            }
            catch (Exception ex)
            {
                ok = false;  //blad serwera
            }

            return ok;
        }//dodaj kanara

        //2 - odnosi sie do kontrolerow
        public static bool czy_poprawne_dane_logowania_uzytkownia2(string login, string password)
        {
            //true - tak login i haslo istnieja
            //false - blad logowania lub wyjatek
            bool wynik = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var answer = (from kontroler in baza.Kontrolerzies
                                  where kontroler.Login == login
                                  select new
                                  {
                                      haslo = kontroler.Haslo
                                  }).FirstOrDefault();

                    if (answer != null)
                    {
                        if (answer.haslo == password)
                        {
                            wynik = true;
                        }
                        else
                        {
                            wynik = false;
                        }
                    }
                    else
                    {
                        wynik = false;
                    }
                }
            }
            catch (Exception ex)
            {
                wynik = false;
            }

            return wynik;
        }

        public static bool czy_token_istnieje2(string token)
        {
            //true - tak login i haslo istnieja
            //false - blad logowania lub wyjatek
            bool istnieje = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var answer = (from kontroler in baza.Kontrolerzies
                                  select kontroler).ToList();

                    if (answer != null)
                    {
                        foreach (var item in answer)
                        {
                            if (item.Token == token)
                            {
                                istnieje = true;
                                break;
                            }
                        }

                        if (istnieje == true)
                        {
                            istnieje = false;
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                istnieje = false;  //blad serwera
            }

            return istnieje;
        }

        public static bool zapisz_token2(string login, string token)
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var answer = (from kontroler in baza.Kontrolerzies
                                  where kontroler.Login == login
                                  select kontroler).ToList();

                    if (answer != null)
                    {
                        //nieoptymalne
                        foreach (var item in answer)
                        {
                            var original = baza.Kontrolerzies.Find(item.Id_Kontrolera);

                            if (original != null)
                            {
                                original.Token = token;
                                baza.SaveChanges();
                                ok = true;
                            }
                            else
                            {
                                ok = false;
                            }
                        }
                    }
                    else
                    {
                        ok = false;
                    }
                }
            }
            catch (Exception ex)
            {
                ok = false;
            }

            return ok;
        }

        public static bool usun_token2(string token)
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var answer = (from kontroler in baza.Kontrolerzies
                                  where kontroler.Token == token
                                  select kontroler).ToList();

                    if (answer != null)
                    {
                        //nieoptymalne
                        foreach (var item in answer)
                        {
                            var original = baza.Kontrolerzies.Find(item.Id_Kontrolera);

                            if (original != null)
                            {
                                original.Token = "usuniety_token";
                                baza.SaveChanges();
                                ok = true;
                            }
                            else
                            {
                                ok = false;
                            }
                        }
                    }
                    else
                    {
                        ok = false;
                    }
                }
            }
            catch (Exception ex)
            {
                ok = false;  //blad serwera
            }

            return ok;
        }//usun_token2

        public static bool pobierzStanKonta(string token, ref double stanKonta)
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var klient = (from k in baza.Kliencis
                                  where k.Token == token
                                  select new
                                  {
                                      stanKonta = k.Saldo
                                  }).FirstOrDefault();

                    if (klient != null)
                    {
                        stanKonta = (double)klient.stanKonta;

                        ok = true;
                    }
                    else
                    {
                        ok = false;
                    }
                }
            }
            catch (Exception)
            {
                ok = false;
            }

            return ok;
        }//pobierzStanKonta
        
        public static bool pobierzListeBiletow(ref List<Ticket> listaBiletow)
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var listaTicketow = (from bilety in baza.Rodzaje_biletów
                        select new
                        {
                            cena = bilety.Cena,
                            nazwa = bilety.Nazwa,
                            id = bilety.Id_Rodzaju,
                            czasTrwania = bilety.Czas_trwania
                        }).ToList();

                    if (listaTicketow != null)
                    {
                        foreach (var item in listaTicketow)
                        {
                            Ticket nowyBilet = new Ticket();
                            nowyBilet.name = item.nazwa;
                            nowyBilet.price = (double)item.cena;
                            nowyBilet.Id = item.id;

                            listaBiletow.Add(nowyBilet);
                        }

                        ok = true;
                    }
                    else
                    {
                        ok = false;
                    }
                }
            }
            catch (Exception)
            {
                ok = false;
            }

            return ok;
        }//pobierzListeBiletow

        public static bool kupBilet(string token, string nazwaBiletu)//todo
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var bilet = (from biletx in baza.Rodzaje_biletów
                                 where biletx.Nazwa == nazwaBiletu
                                 select new
                                 {
                                     czasTrwania = biletx.Czas_trwania,
                                     cena = biletx.Cena,
                                     id = biletx.Id_Rodzaju
                                 }).FirstOrDefault();

                    if (bilet != null)
                    {
                        //sprawdz czy klient ma tyle kasy na koncie
                        var klient = (from k in baza.Kliencis
                                      where k.Token == token
                                      select k).FirstOrDefault();

                        if (klient != null)
                        {
                            //sprawdz czy klient nie ma aktualnego biletu tego nie robie
                            if (klient.Saldo > bilet.cena)
                            {
                                //mozna dokonac transakcji
                                //zabierz z konta kwote biletu
                                klient.Saldo -= bilet.cena;

                                //dodaj bilet dla uzytkownika
                                DateTime teraz = DateTime.UtcNow;
                                DateTime czasTrwania = (DateTime)bilet.czasTrwania;
                                DateTime odjac = new DateTime(2000,1,1);
                                TimeSpan ts = czasTrwania - odjac;                                

                                DateTime DO = teraz.Add(ts);

                                Zakupione_bilety ticket = new Zakupione_bilety();
                                ticket.Id_Rodzaju = bilet.id;
                                ticket.Id_Klienta = klient.Id_Klienta;
                                ticket.Ważność_OD = teraz;
                                ticket.Ważność_DO = DO;

                                baza.Zakupione_bilety.Add(ticket);                                
                                baza.SaveChanges();

                                ok = true;
                            }
                            else
                            {
                                ok = false;
                            }
                        }
                        else
                        {
                            ok = false;
                        }                        
                    }
                    else
                    {
                        ok = false;
                    }
                }
            }
            catch (Exception ex)
            {
                ok = false;
            }

            return ok;
        }//kupBilet

        public static bool czyAktualnyBilet(string token, ref List<Ticket> lista_biletow)
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var klient = (from k in baza.Kliencis
                                  where k.Token == token
                                  select k).FirstOrDefault();

                    if (klient != null)
                    {
                        var ostatniKupiony = (from k in baza.Zakupione_bilety
                                              where k.Id_Klienta == klient.Id_Klienta
                                             select k ).ToList();

                        if (ostatniKupiony != null)
                        {
                            foreach (var item in ostatniKupiony)
                            {
                                //tutaj szukaj nazwy znalezionego biletu
                                var original = baza.Rodzaje_biletów.Find(item.Id_Rodzaju);

                                Ticket nowy = new Ticket();

                                nowy.name = original.Nazwa;
                                //nazwa = original.Nazwa;


                                nowy.date = item.Ważność_DO;
                                //2015:05:22:22:59:00
                                //nowy.date = item.Ważność_DO.Year + ":" + item.Ważność_DO.Month + ":"
                                //    + item.Ważność_DO.Day + ":" + item.Ważność_DO.Hour + ":"
                                //    + item.Ważność_DO.Minute + ":" + item.Ważność_DO.Second;

                                lista_biletow.Add(nowy);
                                nowy = null;
                            }
                            ok = true; 
                        }
                        else
                        {
                            ok = false;
                        }
                    }
                    else
                    {
                        ok = false;
                    }
                }
            }
            catch (Exception ex)
            {
                ok = false;
            }

            return ok;
        }//czyAktualnyBilet

        public static bool dodajKlienta(string imie, string nazwisko, string login, string haslo)            
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    Klienci nowyKlient = new Klienci();
                    nowyKlient.Imie = imie;
                    nowyKlient.Nazwisko = nazwisko;
                    nowyKlient.Login = login;
                    nowyKlient.Haslo = haslo;

                    baza.Kliencis.Add(nowyKlient);
                    baza.SaveChanges();

                    ok = true;
                }//using
            }
            catch (Exception)
            {
                ok = false;
            }

            return ok;
        }//dodajKlienta

        public static bool dodajKontrolera(string imie, string nazwisko, DateTime umowaOd,
            DateTime umowaDo, string login, string haslo)
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    Kontrolerzy kanar = new Kontrolerzy();
                    
                    kanar.Imię = imie;
                    kanar.Nazwisko = nazwisko;
                    kanar.Umowa_OD = umowaOd;
                    kanar.Umowa_DO = umowaDo;
                    kanar.Login = login;
                    kanar.Haslo = haslo;

                    baza.Kontrolerzies.Add(kanar);
                    baza.SaveChanges();

                    ok = true;
                }//using
            }
            catch (Exception)
            {
                ok = false;
            }

            return ok;
        }//dodajKontrolera

        public static bool dodajBilet(string nazwaBiletu, double cena, DateTime czasTrwania)
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    Rodzaje_biletów nowyBilet=new Rodzaje_biletów();

                    nowyBilet.Nazwa = nazwaBiletu;
                    nowyBilet.Cena = (decimal)cena;
                    nowyBilet.Czas_trwania = czasTrwania;

                    baza.Rodzaje_biletów.Add(nowyBilet);
                    baza.SaveChanges();

                    ok = true;
                }//using
            }
            catch (Exception)
            {
                ok = false;
            }

            return ok;
        }//dodajBilet

        public static bool ostatnieBiletyUzytkownika(string tokenKanara, string loginUzytkownika, ref List<Ticket> biletyList)
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    //sprawdz czy token kanara istnieje
                    var kanar = (from k in baza.Kontrolerzies
                        where k.Token == tokenKanara
                        select k).FirstOrDefault();


                    if (kanar != null)
                    {
                        //dla podanego loginu uzytkownika zwroc jego liste biletow
                        var klient = (from k in baza.Kliencis
                                      where k.Login == loginUzytkownika
                                      select k).FirstOrDefault();

                        if (klient != null)
                        {
                            var ostatniKupiony = (from k in baza.Zakupione_bilety
                                                  where k.Id_Klienta == klient.Id_Klienta
                                                  select k).ToList();

                            if (ostatniKupiony != null)
                            {
                                foreach (var item in ostatniKupiony)
                                {
                                    //tutaj szukaj nazwy znalezionego biletu
                                    var original = baza.Rodzaje_biletów.Find(item.Id_Rodzaju);

                                    Ticket nowy = new Ticket();

                                    nowy.name = original.Nazwa;
                                    nowy.date = item.Ważność_DO;

                                    biletyList.Add(nowy);
                                    nowy = null;
                                }
                                ok = true;
                            }
                            else
                            {
                                ok = false;
                            }
                        }
                        else
                        {
                            ok = false;
                        }
                    }//czy null
                    else
                    {
                        ok = false;
                    }
                }//using
            }
            catch (Exception)
            {
                ok = false;
            }

            return ok;

        }

        public static bool usunKlienta(int idKlienta)
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var klient = baza.Kliencis.Find(idKlienta);

                    if (klient != null)
                    {
                        //usun rekordy z zakupione bilety
                        var kupioneBilety = (from bilet in baza.Zakupione_bilety
                                             where bilet.Id_Klienta == klient.Id_Klienta
                                             select bilet).ToList();

                        if (kupioneBilety != null)
                        {

                            foreach (var item in kupioneBilety)
                            {
                                baza.Zakupione_bilety.Attach(item);
                                baza.Zakupione_bilety.Remove(item);
                                
                            }
                            baza.SaveChanges();
                            
                            //usun rekordy z mandaty
                            var mandaty = (from mandat in baza.Mandaties
                                where mandat.Id_Klienta == klient.Id_Klienta
                                select mandat).ToList();

                            if (mandaty != null)
                            {
                                foreach (var item in mandaty)
                                {
                                    baza.Mandaties.Attach(item);
                                    baza.Mandaties.Remove(item);
                                }
                                baza.SaveChanges();

                                //usun klienta
                                baza.Kliencis.Attach(klient);
                                baza.Kliencis.Remove(klient);

                                baza.SaveChanges();

                                ok = true;
                            }
                            else
                            {
                                ok = false;
                            }
                        }
                        else
                        {
                            ok = false;
                        }
                    }
                    else
                    {
                        ok = false;
                    }
                }//using
            }
            catch (Exception)
            {
                ok = false;
            }

            return ok;
        }//usun Klienta

        public static bool usunKanara(int idKanara)
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var kanar = baza.Kontrolerzies.Find(idKanara);

                    if (kanar != null)
                    {
                        //usun rekordy z mandaty
                        var mandaty = (from mandat in baza.Mandaties
                                       where mandat.Id_Kontrolera == kanar.Id_Kontrolera
                                        select mandat).ToList();

                        if (mandaty != null)
                        {
                            foreach (var item in mandaty)
                            {
                                baza.Mandaties.Attach(item);
                                baza.Mandaties.Remove(item);
                            }
                            baza.SaveChanges();

                            //usun klienta
                            baza.Kontrolerzies.Attach(kanar);
                            baza.Kontrolerzies.Remove(kanar);

                            baza.SaveChanges();

                            ok = true;
                        }
                        else
                        {
                            ok = false;
                        }
                    }
                    else
                    {
                        ok = false;
                    }
                }//using
            }
            catch (Exception)
            {
                ok = false;
            }

            return ok;
        }//usun kanara

        public static bool czyKlientIstnieje(string login)
        {
            bool ok = false;

            try
            {
                using (DB_9CA667_projektPZEntities baza = new DB_9CA667_projektPZEntities())
                {
                    var klient = (from klienci in baza.Kliencis
                        where klienci.Login == login
                        select klienci).FirstOrDefault();

                    if (klient != null)//istnieje
                    {
                        ok = true;
                    }
                    else //nie istnieje
                    {
                        ok = false;
                    }
                }//using
            }
            catch (Exception)
            {
                ok = false;
            }

            return ok;
        }
    }//class
}//namespace