using MvcApplication1.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Web;
using System.Web.Hosting;
using System.Web.Http;

namespace MvcApplication1.Controllers
{
    public class WynikiController : ApiController
    {

       
        


        // POST api/logout
        public HttpResponseMessage Post(Result wynik)
        {


              List<Result> lista_wynikow = new List<Result>();
             var fileContents = HostingEnvironment.MapPath(@"~/wyniki.txt");
              int odpowiedz = 0;

            if (!this.ModelState.IsValid)
            {
                //tutaj wywala ogolny blad gdy otrzymal zle dane
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.BadRequest));
            }

            string wyniki = null;
            string[] wynikipraca;





            using (StreamReader input = new StreamReader(fileContents))
            {


                wyniki = input.ReadToEnd();
                
            }
            wyniki = wyniki.Replace("\n","");
            wyniki = wyniki.Replace("\r","");
            wyniki = wyniki.Replace("\\n\\","");
            wyniki = wyniki.Replace("\\r\\","");
            wyniki = wyniki.Replace("\\r\\n","");
            try {

                wynik.name = wynik.name.Replace("\r\n", "");
            }catch(Exception e)
            { }
            

            
                wynikipraca = wyniki.Split(';');
            
            bool czy_bylo = false;
            for(int i= 0; i<wynikipraca.Length-1;i+=3)
            {

                String temp = wynikipraca[i + 2].Replace(" ", "");
                lista_wynikow.Add(new Result(wynikipraca[i], int.Parse(wynikipraca[i+1]) , temp));


            }
            try {
                for (int i = 0; i < lista_wynikow.Count; i++)
                {
                    if (lista_wynikow[i].picture == wynik.picture)

                    {

                        if (lista_wynikow[i].score < wynik.score)
                        {
                            lista_wynikow[i].score = wynik.score;
                            odpowiedz = 1;
                            czy_bylo = true;
                            break;
                            
                        }
                        else
                        {
                            czy_bylo = true;
                            odpowiedz = 2;
                            break;
                        }
                    }

                   


                }
                if (czy_bylo == false)
                {
                    lista_wynikow.Add(new Result(wynik.name, wynik.score, wynik.picture));
                    odpowiedz = 3;

                }
            }
            catch(Exception e)
            {

            }


            List<Result> SortedList = lista_wynikow.OrderByDescending(o => o.score).ToList();
            string zapis = null;

            for (int i = 0; i < lista_wynikow.Count; i++)
            {
                zapis+=SortedList[i].name + ";" + SortedList[i].score + ";" + SortedList[i].picture + ";";

            }
           
            using (StreamWriter outputFile = new StreamWriter(fileContents))
            {

               
                
                    outputFile.WriteLine(zapis);

               
                
            }
           
             

            HttpResponseMessage response = this.Request.CreateResponse(HttpStatusCode.Created, odpowiedz);

            return response;
        }
        public String Get()
        {
            string wyniki;
            
            
            string do_wyslania = null;

            var fileContents = HostingEnvironment.MapPath(@"~/wyniki.txt") ;


            using (StreamReader input = new StreamReader(fileContents))
            {

                wyniki = input.ReadToEnd();
            }






            return wyniki;

        }
    }
}