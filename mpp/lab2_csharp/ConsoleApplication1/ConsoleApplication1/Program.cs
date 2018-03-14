using System;
using System.Linq;
using System.Xml;
using ConsoleApplication1.domain;
using ConsoleApplication1.repository;

namespace ConsoleApplication1
{
    internal class Program
    {
        public static void Main(string[] args)
        {
            XmlDocument doc = new XmlDocument();
            
            doc.Load("../../conf.xml");

            string dbname = "";

            try
            {
                foreach (XmlNode node in doc.DocumentElement.ChildNodes)
                {
                    string text = node.InnerText;
                    if (node.Name.Equals("database"))
                    {
                        dbname = text;
                    }
                }
            }
            catch (NullReferenceException e)
            {
                Console.WriteLine(e.Message);
                return;
            }

            AbstractDbRepository<User, int> db = new DbUserRepository(dbname, "user");

            var lst = db.GetAll();
            foreach (var keyValuePair in lst.ToArray())
            {
                Console.WriteLine("" + keyValuePair.Key + " " + keyValuePair.Value.Username);
            }
            Console.WriteLine("------------");
            
            db.Save(new User(19, "qwe", 3));
            
            lst = db.GetAll();
            foreach (var keyValuePair in lst.ToArray())
            {
                Console.WriteLine("" + keyValuePair.Key + " " + keyValuePair.Value.Username);
            }
            Console.WriteLine("------------");

            db.Delete(18);
            
            lst = db.GetAll();
            foreach (var keyValuePair in lst.ToArray())
            {
                Console.WriteLine("" + keyValuePair.Key + " " + keyValuePair.Value.Username);
            }
            Console.WriteLine("------------");

            db.Update(new User(19, "asd", 5));
            
            lst = db.GetAll();
            foreach (var keyValuePair in lst.ToArray())
            {
                Console.WriteLine("" + keyValuePair.Key + " " + keyValuePair.Value.Username);
            }
            Console.WriteLine("------------");
        }
    }
}