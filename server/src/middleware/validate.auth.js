const validate = (req, res, next) => {
    const { userName, email, password, fullName } = req.body;

    function validEmail(userEmail) {
        // Simpler email validation pattern for mobile
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(userEmail);
    }

    function validUsername(userName) {
        // Allow alphanumeric and underscore, 3-20 characters
        return /^[a-zA-Z0-9_]{3,20}$/.test(userName);
    }

    function validPassword(password) {
        // Simplified password pattern but still secure
        // Min 6 chars, at least 1 uppercase, 1 number
        return /^(?=.*[A-Z])(?=.*\d)[A-Za-z\d@$!%*?&]{6,20}$/.test(password);
    }

    if (!validEmail(email)) {
        return res.status(400).json({
            status: 400,
            message: 'Please enter a valid email address'
        });
    }

    if (!validPassword(password)) {
        return res.status(400).json({
            status: 400,
            message: 'Password must be 6-20 characters long and include at least 1 uppercase letter and 1 number'
        });
    }

    if (req.path === '/register') {
        if (![email, userName, password, fullName].every(Boolean)) {
            return res.status(400).json({
                status: 400,
                message: 'Please fill in all required fields'
            });
        }

        if (!validUsername(userName)) {
            return res.status(400).json({
                status: 400,
                message: 'Username must be 3-20 characters long and can only contain letters, numbers, and underscore'
            });
        }
    }

    if (req.path === '/login') {
        if (![email, password].every(Boolean)) {
            return res.status(400).json({
                status: 400,
                message: 'Please enter both email and password'
            });
        }
    }

    next();
};

export default validate;
