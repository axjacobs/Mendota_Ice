import java.util.Random;

/*
*
* Ice class calculates various data figures based on a data set of 
* Winters.years (1855-2017) and their corresponding number of days with ice.
*
* @author Tony Jacobs
*/
public class Ice
{
	// Winter class is an object that holds data for the 
	// year (1855-2017) and the corresponding number of days with ice.
	public static class Winter
	{
		int year;
		int iceDays;

		// Default constructor for creating a Winter object
		Winter(int year, int iceDays)
		{
			this.year = year;
			this.iceDays = iceDays;
		}
	}

	// Array of Winter objects from the data set
	static Winter[] winters = new Winter[163];
	// Number of winters in the data set
	final static double n = winters.length;	
	// Mean year number from data set
	static double yearMean = 0;
	// Mean number of days with ice
	static double daysMean = 0;
	
	// Prints the year and respective number of ice days for each 
	// winterin the data set.
	public static void f1_Main()
	{
		for (Winter w : winters)
		{
			System.out.println(w.year + " " + w.iceDays);
		}
	}

	// Calculates standard deviation and prints number of winters in data set, 
	// mean number of ice days, and the standard deviation.
	public static void f2_Main()
	{
		double sd = 0;
		for (Winter w : winters)
		{
			sd += Math.pow((w.iceDays-daysMean),2);
		}
		sd /= n-1;
		sd = Math.sqrt(sd);

		System.out.println(n);
		System.out.println(String.format("%.2f", daysMean));
		System.out.println(String.format("%.2f", sd));
	}

	// Calculates mean squared error (MSE) for given Beta 0 and Beta 1 values.
	public static double f3_MSE(final double B0, final double B1)
	{
		double MSE = 0;
		for (Winter w : winters)
		{
			MSE += Math.pow((B0 + (B1*w.year) - w.iceDays),2);
		}
		MSE /= n;

		return MSE;
	}

	// Calculates gradient descent (GD) for B0 value where the gradient is defined by the
	// vector of partial derivatives.
	public static double f4_GD_Beta0(final double B0, final double B1)
	{
		double GD = 0;
		for (Winter w : winters)
		{
			GD += (B0 + (B1*w.year) - w.iceDays);
		}
		GD *= 2/n;

		return GD;
	}

	// Calculate gradient descent for B1 value where the gradient is defined by the
	// vector of partial derivatives.
	public static double f4_GD_Beta1(final double B0, final double B1)
	{
		double GD = 0;
		for (Winter w : winters)
		{
			GD += (B0 + (B1*w.year) - w.iceDays)*w.year;
		}
		GD *= 2/n;

		return GD;
	}

	// Calculate gradient descent for B0 value where gradient is defined by the
	// vector of partial derivatives.
	public static double f5_GD_Beta0(final double N, final double T)
	{
		if (T == 0)
		{
			return 0;
		}
		else
		{
			return f5_GD_Beta0(N, T-1) - N*f4_GD_Beta0(f5_GD_Beta0(N, T-1), f5_GD_Beta1(N, T-1));
		}
	}

	// Calculates gradient descent for B0 value for T iterations.
	public static double f5_GD_Beta1(final double N, final double T)
	{
		if (T == 0)
		{
			return 0;
		}
		else
		{
			return f5_GD_Beta1(N, T-1) - N*f4_GD_Beta1(f5_GD_Beta0(N, T-1), f5_GD_Beta1(N, T-1));
		}
	}

	// Main function for FLAG 500.
	// Prints the B0, B1, and MSE values.
	public static void f5_Main(final double arg1, final double arg2)
	{
		double B0 = 0;
		double B1 = 0;

		for (int t = 1; t < arg2+1; t++)
		{
			B0 = f5_GD_Beta0(arg1, t);
			B1 = f5_GD_Beta1(arg1, t);

			System.out.println(t + " " + String.format("%.2f", B0) 
				+ " " + String.format("%.2f", B1) + " " 
					+ String.format("%.2f", f3_MSE(B0, B1)));
		}
	}

	// Computes B0 value for the closed-form solution using
	// the ordinary least squared formula in one dimmension.
	public static double f6_Beta0()
	{
		return daysMean - f6_Beta1()*yearMean;
	}

	// Computes B1 value for the closed-form solution using
	// the ordinary least squared formula in one dimmension.
	public static double f6_Beta1()
	{		
		double num = 0;
		for (Winter w : winters)
		{
			num += (w.year-yearMean)*(w.iceDays-daysMean);
		}

		double denom = 0;
		for (Winter w : winters)
		{
			denom += Math.pow(w.year-yearMean,2);
		}
		return num/denom;
	}

	// Calculates MSE with Xi element changed with the stdx addition.
	public static double f8_MSE(final double B0, final double B1, final double std)
	{
		double MSE = 0;
		double xi = 0;

		for (Winter w : winters)
		{
			xi = (w.year-yearMean)/std;
			MSE += Math.pow((B0 + (B1*xi) - w.iceDays),2);
		}
		MSE /= n;

		return MSE;
	}

	// Computes B0 value with Xi element changed with the stdx addition.
	public static double f8_GD_Beta0(final double B0, final double B1, final double std)
	{
		double GD = 0;
		double xi = 0;

		for (Winter w : winters)
		{
			xi = (w.year-yearMean)/std;
			GD += (B0 + (B1*xi) - w.iceDays);
		}
		GD *= 2/n;

		return GD;
	}

	// Computes B1 value with Xi element changed with the stdx addition.
	public static double f8_GD_Beta1(final double B0, final double B1, final double std)
	{
		double GD = 0;
		double xi = 0;

		for (Winter w : winters)
		{
			xi = (w.year-yearMean)/std;
			GD += (B0 + (B1*xi) - w.iceDays)*xi;
		}
		GD *= 2/n;

		return GD;
	}

	// Computes B0 value with Xi element changed with the stdx addition
	// for T iterations.
	public static double f8_GD_Beta0T(final double N, final double T, final double std)
	{
		if (T == 0)
		{
			return 0;
		}
		else
		{
			return f8_GD_Beta0T(N, T-1, std) - N*f8_GD_Beta0(f8_GD_Beta0T(N, T-1, std), f8_GD_Beta1T(N, T-1, std), std);
		}
	}

	// Computes B1 value with Xi element changed with the stdx addition
	// for T iterations.
	public static double f8_GD_Beta1T(final double N, final double T, final double std)
	{
		if (T == 0)
		{
			return 0;
		}
		else
		{
			return f8_GD_Beta1T(N, T-1, std) - N*f8_GD_Beta1(f8_GD_Beta0T(N, T-1, std), f8_GD_Beta1T(N, T-1, std), std);
		}
	}

	// Main function for FLAG 800.
	// Prints B0, B1, and MSE for T iterations.
	public static void f8_Main(final double N, final double T)
	{
		double std = 0;
		for (Winter w : winters)
		{
			std += Math.pow(w.year-yearMean,2);
		}
		std *= 1/(n-1);
		std = Math.sqrt(std);

		for (int t = 1; t < T+1; t++)
		{
			double B0 = f8_GD_Beta0T(N, t, std);
			double B1 = f8_GD_Beta1T(N, t, std);

			System.out.println(t + " " + String.format("%.2f", B0) 
				+ " " + String.format("%.2f", B1) + " " 
					+ String.format("%.2f", f8_MSE(B0, B1, std)));
		}
	}

	// Computes B0 value using stochastic gradient descent (SGD).
	public static double f9_GD_Beta0(final double N, final double T, final double std)
	{
		Random rand = new Random();
		Winter w = winters[rand.nextInt(163)];
		double xi = 0;
		if (T == 0)
		{
			return 0;
		}
		else
		{
			xi = (w.year-yearMean)/std;
			return f9_GD_Beta1(N, T-1, std) - N*2*(f9_GD_Beta0(N, T-1, std) + (f9_GD_Beta1(N, T-1, std)*xi) - w.iceDays);
		}
	}

	// Computes B1 value using stochastic gradient descent.
	public static double f9_GD_Beta1(final double N, final double T, final double std)
	{
		Random rand = new Random();
		Winter w = winters[rand.nextInt(163)];
		double xi = 0;
		if (T == 0)
		{
			return 0;
		}
		else
		{
			xi = (w.year-yearMean)/std;
			return f9_GD_Beta1(N, T-1, std) - N*2*(f9_GD_Beta0(N, T-1, std) + (f9_GD_Beta1(N, T-1, std)*xi) - w.iceDays)*xi;
		}
	}

	// Main function for FLAG 900.
	// Prints B0, B1, and MSE values using SGD for T iterations.
	public static void f9_Main(final double N, final double T)
	{
		double std = 0;
		for (Winter w : winters)
		{
			std += Math.pow(w.year-yearMean,2);
		}
		std *= 1/(n-1);
		std = Math.sqrt(std);

		for (int t = 1; t < T+1; t++)
		{
			double B0 = f9_GD_Beta0(N, t, std);
			double B1 = f9_GD_Beta1(N, t, std);

			System.out.println(t + " " + String.format("%.2f", B0) 
				+ " " + String.format("%.2f", B1) + " " 
					+ String.format("%.2f", f8_MSE(B0, B1, std)));
		}
	}

	// Determines which function to call based on FLAG
	// from console input when FLAG is the only input.
	public static void parseFlag(final int FLAG)
	{
		if (FLAG == 100)
		{
			f1_Main();
		}
		else if (FLAG == 200)
		{
			f2_Main();
		}
		else if (FLAG == 600)
		{
			double B1 = f6_Beta1();
			double B0 = f6_Beta0();
			System.out.println(String.format("%.2f", B0) + " " 
				+ String.format("%.2f", B1) + " " 
					+ String.format("%.2f", f3_MSE(B0, B1)));
		}
	}

	// Determines which function to call based on FLAG
	// from console input when the console inputs are FLAG
	// and arg1.
	public static void parseFlag(final int FLAG, final double arg1)
	{
		if (FLAG == 700)
		{
			System.out.println(String.format("%.2f", f6_Beta1()*arg1 + f6_Beta0()));
		}
	}

	// Determines which function to call based on FLAG
	// from console input when the console inputs are 
	// FLAG, arg1, and arg2.
	public static void parseFlag(final int FLAG, final double arg1, final double arg2)
	{
		if (FLAG == 300)
		{
			System.out.println(String.format("%.2f", f3_MSE(arg1, arg2)));
		}
		else if (FLAG == 400)
		{
			System.out.println(String.format("%.2f", f4_GD_Beta0(arg1, arg2)));
			System.out.println(String.format("%.2f", f4_GD_Beta1(arg1, arg2)));
		}
		else if (FLAG == 500)
		{
			f5_Main(arg1, arg2);
		}
		else if (FLAG == 800)
		{
			f8_Main(arg1, arg2);
		}
		else if (FLAG == 900)
		{
			f9_Main(arg1, arg2);
		}
	}

	public static void main(String args[])
	{

		if (args.length < 1)
		{
			System.out.println("Format command line as 'java Ice FLAG [arg1 arg2]' where [arg1 arg2] are optional.");
			System.exit(0);
		}
		else
		{
			initializeArray();
			final int FLAG = Integer.parseInt(args[0]);

			if (args.length == 1)
			{
				parseFlag(FLAG);
			}
			else if (args.length == 2)
			{
				parseFlag(FLAG, Double.parseDouble(args[1]));
			}
			else if (args.length == 3)
			{
				parseFlag(FLAG, Double.parseDouble(args[1]), Double.parseDouble(args[2]));
			}
		}
	}

	// Fills an array of Winter objects with years and number of days with 
	// ice taken from http://www.aos.wisc.edu/~sco/lakes/Mendota-ice.html.
	// Also sets global mean variables for the data set.
	public static void initializeArray()
	{
		winters[0] = new Winter(1855, 118);
		winters[1] = new Winter(1856, 151);
		winters[2] = new Winter(1857, 121);
		winters[3] = new Winter(1858, 96);
		winters[4] = new Winter(1859, 110);
		winters[5] = new Winter(1860, 117);
		winters[6] = new Winter(1861, 132);
		winters[7] = new Winter(1862, 104);
		winters[8] = new Winter(1863, 125);
		winters[9] = new Winter(1864, 118);
		winters[10] = new Winter(1865, 125);
		winters[11] = new Winter(1866, 123);
		winters[12] = new Winter(1867, 110);
		winters[13] = new Winter(1868, 127);
		winters[14] = new Winter(1869, 131);
		winters[15] = new Winter(1870, 99);
		winters[16] = new Winter(1871, 126);
		winters[17] = new Winter(1872, 144);
		winters[18] = new Winter(1873, 136);
		winters[19] = new Winter(1874, 126);
		winters[20] = new Winter(1875, 91);
		winters[21] = new Winter(1876, 130);
		winters[22] = new Winter(1877, 62);
		winters[23] = new Winter(1878, 112);
		winters[24] = new Winter(1879, 99);
		winters[25] = new Winter(1880, 161);
		winters[26] = new Winter(1881, 78);
		winters[27] = new Winter(1882, 124);
		winters[28] = new Winter(1883, 119);
		winters[29] = new Winter(1884, 124);
		winters[30] = new Winter(1885, 128);
		winters[31] = new Winter(1886, 131);
		winters[32] = new Winter(1887, 113);
		winters[33] = new Winter(1888, 88);
		winters[34] = new Winter(1889, 75);
		winters[35] = new Winter(1890, 111);
		winters[36] = new Winter(1891, 97);
		winters[37] = new Winter(1892, 112);
		winters[38] = new Winter(1893, 101);
		winters[39] = new Winter(1894, 101);
		winters[40] = new Winter(1895, 91);
		winters[41] = new Winter(1896, 110);
		winters[42] = new Winter(1897, 100);
		winters[43] = new Winter(1898, 130);
		winters[44] = new Winter(1899, 111);
		winters[45] = new Winter(1900, 107);
		winters[46] = new Winter(1901, 105);
		winters[47] = new Winter(1902, 89);
		winters[48] = new Winter(1903, 126);
		winters[49] = new Winter(1904, 108);
		winters[50] = new Winter(1905, 97);
		winters[51] = new Winter(1906, 94);
		winters[52] = new Winter(1907, 83);
		winters[53] = new Winter(1908, 106);
		winters[54] = new Winter(1909, 98);
		winters[55] = new Winter(1910, 101);
		winters[56] = new Winter(1911, 108);
		winters[57] = new Winter(1912, 99);
		winters[58] = new Winter(1913, 88);
		winters[59] = new Winter(1914, 115);
		winters[60] = new Winter(1915, 102);
		winters[61] = new Winter(1916, 116);
		winters[62] = new Winter(1917, 115);
		winters[63] = new Winter(1918, 82);
		winters[64] = new Winter(1919, 110);
		winters[65] = new Winter(1920, 81);
		winters[66] = new Winter(1921, 96);
		winters[67] = new Winter(1922, 125);
		winters[68] = new Winter(1923, 104);
		winters[69] = new Winter(1924, 105);
		winters[70] = new Winter(1925, 124);
		winters[71] = new Winter(1926, 103);
		winters[72] = new Winter(1927, 106);
		winters[73] = new Winter(1928, 96);
		winters[74] = new Winter(1929, 107);
		winters[75] = new Winter(1930, 98);
		winters[76] = new Winter(1931, 65);
		winters[77] = new Winter(1932, 115);
		winters[78] = new Winter(1933, 91);
		winters[79] = new Winter(1934, 94);
		winters[80] = new Winter(1935, 101);
		winters[81] = new Winter(1936, 121);
		winters[82] = new Winter(1937, 105);
		winters[83] = new Winter(1938, 97);
		winters[84] = new Winter(1939, 105);
		winters[85] = new Winter(1940, 96);
		winters[86] = new Winter(1941, 82);
		winters[87] = new Winter(1942, 116);
		winters[88] = new Winter(1943, 114);
		winters[89] = new Winter(1944, 92);
		winters[90] = new Winter(1945, 98);
		winters[91] = new Winter(1946, 101);
		winters[92] = new Winter(1947, 104);
		winters[93] = new Winter(1948, 96);
		winters[94] = new Winter(1949, 109);
		winters[95] = new Winter(1950, 122);
		winters[96] = new Winter(1951, 114);
		winters[97] = new Winter(1952, 81);
		winters[98] = new Winter(1953, 85);
		winters[99] = new Winter(1954, 92);
		winters[100] = new Winter(1955, 114);
		winters[101] = new Winter(1956, 111);
		winters[102] = new Winter(1957, 95);
		winters[103] = new Winter(1958, 126);
		winters[104] = new Winter(1959, 105);
		winters[105] = new Winter(1960, 108);
		winters[106] = new Winter(1961, 117);
		winters[107] = new Winter(1962, 112);
		winters[108] = new Winter(1963, 113);
		winters[109] = new Winter(1964, 120);
		winters[110] = new Winter(1965, 65);
		winters[111] = new Winter(1966, 98);
		winters[112] = new Winter(1967, 91);
		winters[113] = new Winter(1968, 108);
		winters[114] = new Winter(1969, 113);
		winters[115] = new Winter(1970, 110);
		winters[116] = new Winter(1971, 105);
		winters[117] = new Winter(1972, 97);
		winters[118] = new Winter(1973, 105);
		winters[119] = new Winter(1974, 107);
		winters[120] = new Winter(1975, 88);
		winters[121] = new Winter(1976, 115);
		winters[122] = new Winter(1977, 123);
		winters[123] = new Winter(1978, 118);
		winters[124] = new Winter(1979, 99);
		winters[125] = new Winter(1980, 93);
		winters[126] = new Winter(1981, 96);
		winters[127] = new Winter(1982, 54);
		winters[128] = new Winter(1983, 111);
		winters[129] = new Winter(1984, 85);
		winters[130] = new Winter(1985, 107);
		winters[131] = new Winter(1986, 89);
		winters[132] = new Winter(1987, 87);
		winters[133] = new Winter(1988, 97);
		winters[134] = new Winter(1989, 93);
		winters[135] = new Winter(1990, 88);
		winters[136] = new Winter(1991, 99);
		winters[137] = new Winter(1992, 108);
		winters[138] = new Winter(1993, 94);
		winters[139] = new Winter(1994, 74);
		winters[140] = new Winter(1995, 119);
		winters[141] = new Winter(1996, 102);
		winters[142] = new Winter(1997, 47);
		winters[143] = new Winter(1998, 82);
		winters[144] = new Winter(1999, 53);
		winters[145] = new Winter(2000, 115);
		winters[146] = new Winter(2001, 21);
		winters[147] = new Winter(2002, 89);
		winters[148] = new Winter(2003, 80);
		winters[149] = new Winter(2004, 101);
		winters[150] = new Winter(2005, 95);
		winters[151] = new Winter(2006, 66);
		winters[152] = new Winter(2007, 106);
		winters[153] = new Winter(2008, 97);
		winters[154] = new Winter(2009, 87);
		winters[155] = new Winter(2010, 109);
		winters[156] = new Winter(2011, 57);
		winters[157] = new Winter(2012, 87);
		winters[158] = new Winter(2013, 117);
		winters[159] = new Winter(2014, 91);
		winters[160] = new Winter(2015, 62);
		winters[161] = new Winter(2016, 65);
		winters[162] = new Winter(2017, 94);

		for (Winter w : winters)
		{
			yearMean += w.year;
			daysMean += w.iceDays;
		}
		yearMean /= n;
		daysMean /= n;
	}
}