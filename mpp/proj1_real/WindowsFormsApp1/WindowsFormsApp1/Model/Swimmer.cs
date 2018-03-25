using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WindowsFormsApp1.Model
{
    class Swimmer
    {
        int id;
        string name;
        int age;
        List<Competition> competitions;

        public Swimmer(int id, string name, int age, List<Competition> competitions)
        {
            this.id = id;
            this.name = name;
            this.age = age;
            this.competitions = competitions;
        }

        public Swimmer()
        {
        }

        public int Id
        {
            get => id;
            set => id = value;
        }

        public string Name
        {
            get => name;
            set => name = value;
        }

        public int Age
        {
            get => age;
            set => age = value;
        }

        public List<Competition> Competitions
        {
            get => competitions;
            set => competitions = value;
        }
    }
}
