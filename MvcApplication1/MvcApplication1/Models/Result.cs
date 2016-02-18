using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace MvcApplication1.Models
{
    public class Result
    {


        public string name { get; set; }
        public int score { get; set; }
        public string picture { get; set; }

        public Result(string name, int score, string picture)
        {
            this.name = name;
            this.score = score;
            this.picture = picture;
        }
    }
}