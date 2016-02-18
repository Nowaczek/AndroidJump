using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;


namespace MvcApplication1
{
    static class Token
    {
        private static Random r = new Random(Environment.TickCount);

        public static string RandomString(int length)
        {
            string chars = "0123456789abcdefghijklmnopqrstuvwxyz";
            StringBuilder builder = new StringBuilder(length);

            for (int i = 0; i < length; ++i)
                builder.Append(chars[r.Next(chars.Length)]);

            return builder.ToString();
        }

        public static string generuj_token()
        {
            //1. wygenereuj losowy string danych
            //2. sprawdź czy podany token istnieje
            string s = RandomString(20);

            while(Zapytania.czy_token_istnieje(s)==true)
            {
                s = RandomString(20);
            }

            return s;
        }

        public static string generuj_token2()//w tabeli kontrolerow
        {
            //1. wygenereuj losowy string danych
            //2. sprawdź czy podany token istnieje
            string s = RandomString(20);

            while (Zapytania.czy_token_istnieje2(s) == true)
            {
                s = RandomString(20);
            }

            return s;
        }
    }
}