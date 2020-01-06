using System;
using System.Linq;
using System.Collections.Generic;

namespace OpenValidationFramework_CSharp
{

    public class HUMLFramework {

        public List<RuleItem> Rules { get; set; }

        public HUMLFramework() {
            this.Rules = new List<RuleItem>();
        }


        //VALIDATION METHODS

        public ValidationRule<T> appendRule<T>(string name, string[] fields, string error, Func<T, bool> validationFunc, Boolean disabled)
        {
            var rule = new ValidationRule<T>(name, fields, error, validationFunc, disabled);
            this.Rules.Add(rule);
            return rule;
        }


        public OpenValidationSummary validate<T>(T model)
        {
            OpenValidationSummary summary = new OpenValidationSummary();

            foreach (var rule in this.getEnabledRules<T>())
            {
                try
                {
                    ValidationResult<T> result = rule.Validate(model);

                    if (result.HasErrors)
                    {
                        summary.AppendError(result.Rule.Error, result.Rule.Fields);
                    }
                }
                catch (Exception ex)
                {
                    summary.AppendError(ex.Message, null);
                }
            }

            return summary;
        }

        public ValidationResult<T> ValidateRule<T>(T model, String ruleName)
        {
            var preCondition = this.GetRule(model, ruleName);
            ValidationResult<T> preConditionResult = preCondition.Validate(model);

            if (preConditionResult.HasErrors)
                return new ValidationResult<T>(preConditionResult.Rule);

            return null;
        }

        public Boolean ExecuteRuleCondition<T>(T model, String ruleName)
        {
            var preCondition = this.GetRule(model, ruleName);
            return preCondition.ExecuteCondition(model);
        }

        public IEnumerable<ValidationRule<T>> getEnabledRules<T>()
        {
            return this.Rules.OfType<ValidationRule<T>>().Where(r => !r.Disabled && r.HasError);
        }

        public Variable<TM, R> CreateVariable<TM, R>(String name, R value)
        {
            return new Variable<TM, R>(name, value);
        }

        public Variable<TM, R> CreateVariable<TM, R>(String name, Func<TM, R> value)
        {
            return new Variable<TM, R>(name, value);
        }

        public ValidationRule<T> GetRule<T>(T model, String ruleName)
        {
            var rule = this.Rules.Where(r => String.IsNullOrWhiteSpace(r.Name) &&
                                        r.Name.Equals(ruleName, StringComparison.InvariantCultureIgnoreCase))
                                 .FirstOrDefault();


            if (rule != null)
                return rule as ValidationRule<T>;

            throw new Exception("Rule with name: \"" + ruleName + "\" could not be found.");
        }

        //END OF VALIDATION METHODS

        #region comparison methods

        public bool EQUALS<T>(T leftOperand, T rightOperand)
        {
            return EqualityComparer<T>.Default.Equals(leftOperand, rightOperand);
        }

        public bool EQUALS(Object leftOperand, Object rightOperand)
        {
            if (IsNumericType(leftOperand) && IsNumericType(rightOperand))
            {
                return EqualityComparer<Double>.Default.Equals((double)leftOperand, (double)rightOperand);
            }

            return EqualityComparer<Object>.Default.Equals(leftOperand, rightOperand);
        }

        public bool NOT_EQUALS<T>(T leftOperand, T rightOperand)
        {
            return !EQUALS(leftOperand, rightOperand);
        }
        
        public static bool IsNumericType(object o)
        {   
            switch (Type.GetTypeCode(o.GetType()))
            {
                case TypeCode.Byte:
                case TypeCode.SByte:
                case TypeCode.UInt16:
                case TypeCode.UInt32:
                case TypeCode.UInt64:
                case TypeCode.Int16:
                case TypeCode.Int32:
                case TypeCode.Int64:
                case TypeCode.Decimal:
                case TypeCode.Double:
                case TypeCode.Single:
                    return true;
                default:
                    return false;
            }
        }
        
        public bool EMPTY<T>(T operand)
        {
            return (operand == null);
        }
        
        public bool NOT_EMPTY<T>(T operand) {
            return !EMPTY(operand);
        }

        public bool LESS_THAN<T>(T leftOperand, T rightOperand) where T : IComparable<T>
        {
            return leftOperand.CompareTo(rightOperand) < 0;
        }

        public bool GREATER_THAN<T>(T leftOperand, T rightOperand) where T : IComparable<T>
        {
            return leftOperand.CompareTo(rightOperand) > 0;
        }

        public bool LESS_OR_EQUALS<T>(T leftOperand, T rightOperand) where T : IComparable<T>
        {
            var res = leftOperand.CompareTo(rightOperand);
            return res <= 0;
        }

        public bool GREATER_OR_EQUALS<T>(T leftOperand, T rightOperand) where T : IComparable<T>
        {
            var res = leftOperand.CompareTo(rightOperand);
            return res >= 0;
        }

        public bool ONE_OF<T>(T leftOperand, params T[] rightOperand)
        {
            return rightOperand.Any(i => i.Equals(leftOperand));
        }

        public bool AT_LEAST_ONE_OF<T>(T leftOperand, params T[] rightOperand)
        {
            return ONE_OF(leftOperand, rightOperand);
        }

        public bool NONE_OF<T>(T leftOperand, params T[] rightOperand)
        {
            return !AT_LEAST_ONE_OF(leftOperand, rightOperand);
        }
        
        public Decimal SUM_OF<T>(params T[] operand)
        {
            List<Decimal> dec = new List<decimal>();
            operand.ToList().ForEach(x => dec.Add(Convert.ToDecimal(x)));
            return dec.Sum();
        }

        public T[] getArrayOf<T>(IEnumerable<T> operand)
        {
            return operand.ToArray();
        }

        public int sizeOf<T>(IEnumerable<T> operand)
        {
            return operand.Count();
        }

        public T atPosition<T>(IEnumerable<T> operand, int position)
        {
            return operand.ElementAt(position);
        }

        public T[] FIRST<T>(IEnumerable<T> operand, int amount)
        {
            return operand.Take(amount).ToArray();
        }
        
        public T FIRST<T>(IEnumerable<T> operand)
        {
            return FIRST(operand, 1)[0];
        }
        
        public T[] FIRST<T>(IEnumerable<T> operand, Func<T,bool> filter, int amount)
        {
            return operand.Where(filter).Take(amount).ToArray();
        }
        
        public T LAST<T>(IEnumerable<T> operand, Func<T,bool> filter)
        {
            return operand.Where(filter).Last();
        }
        
        public T[] LAST<T>(IEnumerable<T> operand, Func<T,bool> filter, int amount)
        {
            return operand.Where(filter).TakeLast(amount).ToArray();
        }
        
        public T[] LAST<T>(IEnumerable<T> operand, int amount)
        {
            return operand.TakeLast(amount).ToArray();
        }
        
        public T[] LAST<T>(IEnumerable<T> operand, double amount)
        {
            return operand.TakeLast((int) amount).ToArray();
        }
        
        public T LAST<T>(IEnumerable<T> operand)
        {
            return operand.Last();
        }

        public T[] WHERE<T>(T[] operand, Func<T,bool> filter)
        {
            return operand.Where(filter).ToArray();
        }

        public static IEnumerable<TSource> DistinctBy<TSource, TKey>(IEnumerable<TSource> source, Func<TSource, TKey> keySelector)
        {
            HashSet<TKey> seenKeys = new HashSet<TKey>();
            foreach (TSource element in source)
            {
                if (seenKeys.Add(keySelector(element)))
                {
                    yield return element;
                }
            }
        }

        #endregion

        #region inner classes

        public interface IOpenValidator {
            String ValidatorID { get; }
            OpenValidationSummary Validate(Object model);
        }

        public abstract class RuleItem
        {
            public string Name { get; set; }
        }

        public class ValidationRule<T> : RuleItem
        {
            private Func<T, bool> _conditionFnc;

            public String Name
            {
                get { return ((RuleItem) this).Name;}
                set { ((RuleItem) this).Name = value; }
            }
            public String[] Fields { get; set; }
            public String Error { get; set; }
            public bool Disabled { get; set; }

            public Boolean HasError
            {
                get
                {
                    return !String.IsNullOrEmpty(this.Error);
                }
            }

            public ValidationRule(String name, String[] fields, Func<T, bool> conditionFnc, Boolean disabled)
            {
                this.Name = name;
                this.Fields = fields;
                this._conditionFnc = conditionFnc;
                this.Disabled = disabled;
            }

            public ValidationRule(String name, String[] fields, String error, Func<T, bool> conditionFnc, Boolean disabled)
            {
                this.Name = name;
                this.Fields = fields;
                this.Error = error;
                this._conditionFnc = conditionFnc;
                this.Disabled = disabled;
            }

            public ValidationResult<T> Validate(T model)
            {
                return this.CreateResult(model, this, this._conditionFnc);
            }

            public ValidationResult<T> CreateResult(T model, ValidationRule<T> rule, Func<T, bool> isInvalidFnc)
            {
                return isInvalidFnc(model) ?
                    new ValidationResult<T>(rule) : new ValidationResult<T>();
            }

            public Boolean ExecuteCondition(T model)
            {
                return this._conditionFnc(model);
            }
        }

        public class Variable<TM, T> : RuleItem
        {
            private Func<TM, T> _valueFunc;

            public Variable(String name, T value)
            {
                this.Name = name;
                this._valueFunc = m => value;
            }

            public Variable(String name, Func<TM, T> valueFunc)
            {
                this.Name = name;
                this._valueFunc = valueFunc;
            }

            public T GetValue(TM model)
            {
                return this._valueFunc(model);
            }
        }

        public class OpenValidationSummary
        {
            public OpenValidationSummaryError[] Errors { get; set; }
            public string[] Fields
            {
                get
                {
                    return this.GeErrors().Where(e => e.Fields != null).SelectMany(e => e.Fields).ToArray();
                }
            }

            public OpenValidationSummary()
            {
                this.Errors = new OpenValidationSummaryError[0];
            }

            public void AppendError(string error, string[] fields)
            {
                var errors = new List<OpenValidationSummaryError>();
                errors.AddRange(this.Errors);
                errors.Add(new OpenValidationSummaryError(error, fields));

                this.Errors = errors.ToArray();
            }

            public bool HasErrors
            {
                get
                {
                    return this.Errors != null && this.Errors.Length > 0;
                }
            }

            public IEnumerable<OpenValidationSummaryError> GeErrors()
            {
                return (this.Errors != null) ? DistinctBy(this.Errors, p => new { p.Error }) : new OpenValidationSummaryError[] { };
            }
        }

        public class OpenValidationSummaryError
        {
            public string Error { get; set; }
            public string[] Fields { get; set; }

            public OpenValidationSummaryError(string error, string[] fields)
            {
                this.Error = error;
                this.Fields = fields;
            }
        }

        public class ValidationResult<T>
        {
            public ValidationRule<T> Rule { get; set; }

            public ValidationResult(ValidationRule<T> rule)
            {
                this.Rule = rule;
            }

            public ValidationResult() { }

            public Boolean HasErrors
            {
                get
                {
                    return (GetError() != null);
                }
            }

            public String GetError()
            {
                return (Rule != null) ? Rule.Error : null;
            }
        }
    }

    #endregion


}

