package rules;

public interface IBusinessRule<T> {
	void validate(T request);
}