namespace WindowsFormsApp1.Model
{
    public interface IHasId<T>
    {
        T Id { get; set; }
    }
}