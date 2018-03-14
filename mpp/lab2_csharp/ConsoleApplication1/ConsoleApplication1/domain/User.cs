using ConsoleApplication1.repository;

namespace ConsoleApplication1.domain
{
    public class User : IHasId<int>
    {
        private int _id;
        private string _username;
        private int _age;

        public User(int id, string username, int age)
        {
            this._id = id;
            this._username = username;
            this._age = age;
        }

        public int Id
        {
            get => _id;
            set => _id = value;
        }

        public string Username
        {
            get => _username;
            set => _username = value;
        }

        public int Age
        {
            get => _age;
            set => _age = value;
        }
    }
}